package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.jwt.dto.TokenInfo;
import im.greenmate.api.domain.jwt.repository.RefreshTokenRepository;
import im.greenmate.api.domain.user.dto.request.TokenReissueRequest;
import im.greenmate.api.domain.user.dto.response.TokenReissueResponse;
import im.greenmate.api.domain.user.exception.InvalidRefreshTokenException;
import im.greenmate.api.global.security.Account;
import im.greenmate.api.global.security.UserDetailsImpl;
import im.greenmate.api.global.util.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthReissueServiceTest {
    @InjectMocks
    private AuthReissueService authReissueService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueSuccess() {
        // given
        TokenReissueRequest request = new TokenReissueRequest("accessToken", "refreshToken");
        Account account = new Account("username", "password");
        UserDetails principal = new UserDetailsImpl(account, null);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, "", null);
        TokenInfo tokenInfo = new TokenInfo("Bear", "newAccessToken", "newRefreshToken", null, null);

        when(jwtTokenProvider.getAuthentication(request.getAccessToken())).thenReturn(authenticationToken);
        when(jwtTokenProvider.getUsername(request.getAccessToken())).thenReturn(account.getUsername());
        when(jwtTokenProvider.validateToken(request.getRefreshToken())).thenReturn(true);
        when(jwtTokenProvider.generateToken(authenticationToken)).thenReturn(tokenInfo);
        when(refreshTokenRepository.existsByKey(request.getRefreshToken())).thenReturn(false);

        // when
        TokenReissueResponse result = authReissueService.reissue(request);

        // then
        assertThat(result.getGrantType()).isEqualTo(tokenInfo.getGrantType());
        assertThat(result.getAccessToken()).isEqualTo(tokenInfo.getAccessToken());
        assertThat(result.getRefreshToken()).isEqualTo(tokenInfo.getRefreshToken());
    }

    @Test
    @DisplayName("토큰 재발급 실패 - refreshToken 검증 실패")
    void reissueFailByInvalidateToken() {
        // given
        TokenReissueRequest request = new TokenReissueRequest("accessToken", "refreshToken");
        when(jwtTokenProvider.validateToken(request.getRefreshToken())).thenReturn(false);

        // expected
        assertThrows(InvalidRefreshTokenException.class,
                () -> authReissueService.reissue(request));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - refreshToken이 만료 저장소에 저장되어 있음")
    void reissueFailByExistInExpiredRepository() {
        // given
        TokenReissueRequest request = new TokenReissueRequest("accessToken", "refreshToken");
        when(jwtTokenProvider.validateToken(request.getRefreshToken())).thenReturn(true);
        when(refreshTokenRepository.existsByKey(request.getRefreshToken())).thenReturn(true);

        // expected
        assertThrows(InvalidRefreshTokenException.class,
                () -> authReissueService.reissue(request));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - AccessToken과 RefreshToken의 username이 다름")
    void reissueFailByNotEqualUsername() {
        // given
        TokenReissueRequest request = new TokenReissueRequest("accessToken", "refreshToken");
        Account account = new Account("username", "password");
        UserDetails principal = new UserDetailsImpl(account, null);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, "", null);

        when(jwtTokenProvider.getAuthentication(request.getAccessToken())).thenReturn(authenticationToken);
        when(jwtTokenProvider.getUsername(request.getAccessToken())).thenReturn(account.getUsername() + "wrong");
        when(jwtTokenProvider.validateToken(request.getRefreshToken())).thenReturn(true);
        when(refreshTokenRepository.existsByKey(request.getRefreshToken())).thenReturn(false);

        // expected
        assertThrows(InvalidRefreshTokenException.class,
                () -> authReissueService.reissue(request));
    }
}