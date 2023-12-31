package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.jwt.entity.RefreshToken;
import im.greenmate.api.domain.jwt.repository.RefreshTokenRepository;
import im.greenmate.api.domain.user.dto.request.LogoutRequest;
import im.greenmate.api.global.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLogoutService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void logout(LogoutRequest request) {
        String username = jwtTokenProvider.getUsername(request.getRefreshToken());
        Date expiredAt = jwtTokenProvider.getExpirationDate(request.getRefreshToken());
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(request.getRefreshToken())
                .username(username)
                .expiredAt(expiredAt)
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}
