package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.jwt.entity.RefreshToken;
import im.greenmate.api.domain.jwt.repository.RefreshTokenRepository;
import im.greenmate.api.domain.user.dto.request.LogoutRequest;
import im.greenmate.api.global.util.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLogoutServiceTest {

    @InjectMocks
    private UserLogoutService userLogoutService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccess() {
        // given
        String username = "username";
        Date expiredAt = new Date();
        LogoutRequest request = new LogoutRequest("refreshToken");
        when(jwtTokenProvider.getUsername(request.getRefreshToken())).thenReturn(username);
        when(jwtTokenProvider.getExpirationDate(request.getRefreshToken())).thenReturn(expiredAt);

        // when
        userLogoutService.logout(request);

        // then
        ArgumentCaptor<RefreshToken> argument = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository, times(1)).save(argument.capture());
        assertThat(argument.getValue().getRefreshToken()).isEqualTo(request.getRefreshToken());
        assertThat(argument.getValue().getUsername()).isEqualTo(username);
        assertThat(argument.getValue().getExpiredAt()).isEqualTo(expiredAt);
    }
}