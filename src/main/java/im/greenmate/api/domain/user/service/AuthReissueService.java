package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.jwt.dto.TokenInfo;
import im.greenmate.api.domain.jwt.entity.RefreshToken;
import im.greenmate.api.domain.jwt.repository.RefreshTokenRepository;
import im.greenmate.api.domain.user.dto.request.TokenReissueRequest;
import im.greenmate.api.domain.user.dto.response.TokenReissueResponse;
import im.greenmate.api.domain.user.exception.InvalidRefreshTokenException;
import im.greenmate.api.domain.user.exception.RefreshTokenNotFoundException;
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

        // 2. Access Token 에서 username 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(authentication.getName())
                .orElseThrow(RefreshTokenNotFoundException::new);

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getRefreshToken().equals(tokenDto.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        // 5. 새로운 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken =
                refreshToken.updateValue(tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpiredAt());
        refreshTokenRepository.save(newRefreshToken);

        return TokenReissueResponse.of(tokenInfo);
    }
}
