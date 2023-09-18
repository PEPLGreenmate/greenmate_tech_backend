package im.greenmate.api.domain.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String username;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Date expiredAt;

    @Builder
    public RefreshToken(String username, String refreshToken, Date expiredAt) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }
}
