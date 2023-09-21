package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.jwt.dto.TokenInfo;
import im.greenmate.api.domain.jwt.entity.RefreshToken;
import im.greenmate.api.domain.jwt.repository.RefreshTokenRepository;
import im.greenmate.api.domain.user.dto.request.TokenReissueRequest;
import im.greenmate.api.domain.user.dto.response.TokenReissueResponse;
import im.greenmate.api.domain.user.exception.InvalidRefreshTokenException;
import im.greenmate.api.global.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthReissueService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenReissueResponse reissue(TokenReissueRequest tokenDto) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(tokenDto.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        if (refreshTokenRepository.existsByKey(tokenDto.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
        String username = jwtTokenProvider.getUsername(tokenDto.getAccessToken());
        if (!username.equals(authentication.getName())) {
            throw new InvalidRefreshTokenException();
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenDto.getRefreshToken())
                .username(username)
                .expiredAt(jwtTokenProvider.getExpirationDate(tokenDto.getRefreshToken()))
                .build();
        refreshTokenRepository.save(refreshToken);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        return TokenReissueResponse.of(tokenInfo);
    }
}
