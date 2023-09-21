package im.greenmate.api.domain.jwt.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class RefreshToken {

    private final String refreshToken;
    private final String username;
    private final Date expiredAt;

    @Builder
    public RefreshToken(String username, String refreshToken, Date expiredAt) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }
}
