package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.jwt.dto.TokenInfo;
import im.greenmate.api.domain.jwt.entity.RefreshToken;
import im.greenmate.api.domain.user.dto.response.LoginResponse;
import im.greenmate.api.global.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .expiredAt(tokenInfo.getRefreshTokenExpiredAt())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return LoginResponse.of(tokenInfo);
    }
}
