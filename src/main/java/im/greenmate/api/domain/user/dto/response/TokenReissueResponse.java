package im.greenmate.api.domain.user.dto.response;

import im.greenmate.api.domain.jwt.dto.TokenInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenReissueResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;

    public static TokenReissueResponse of(TokenInfo tokenDto) {
        return TokenReissueResponse.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }
}
