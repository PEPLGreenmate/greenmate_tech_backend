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

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthReissueService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenReissueResponse reissue(TokenReissueRequest tokenDto) {
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
        String username = jwtTokenProvider.getUsername(tokenDto.getAccessToken());

        validateRefreshToken(tokenDto.getRefreshToken(), username, authentication);
        saveRefreshToken(tokenDto.getRefreshToken(), username);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        return TokenReissueResponse.of(tokenInfo);
    }

    private void validateRefreshToken(String refreshToken, String username, Authentication authentication) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        if (refreshTokenRepository.existsByKey(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        if (!username.equals(authentication.getName())) {
            throw new InvalidRefreshTokenException();
        }
    }

    private void saveRefreshToken(String refreshToken, String username) {
        Date expiredAt = jwtTokenProvider.getExpirationDate(refreshToken);
        RefreshToken refreshTokenObject = RefreshToken.builder()
                .refreshToken(refreshToken)
                .username(username)
                .expiredAt(expiredAt)
                .build();
        refreshTokenRepository.save(refreshTokenObject);
    }
}
