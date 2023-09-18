package im.greenmate.api.global.util;

import im.greenmate.api.domain.jwt.dto.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final String issuer;
    private final Long accessTokenValidityInMilliseconds;
    private final Long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.issuer}") String issuer,
                            @Value("${jwt.access-token-expiration-seconds}") Long accessTokenExpirationSeconds,
                            @Value("${jwt.refresh-token-expiration-seconds}") Long refreshTokenExpirationSeconds) {
        this.accessTokenValidityInMilliseconds = accessTokenExpirationSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenExpirationSeconds * 1000;
        this.issuer = issuer;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(Authentication authentication) {
        long now = new Date().getTime();
        Date accessTokenExpiredAt = createAccessTokenExpirationTime(now);
        Date refreshTokenExpiredAt = createRefreshTokenExpirationTime(now);

        String accessToken = createAccessToken(authentication, accessTokenExpiredAt);
        String refreshToken = createRefreshToken(refreshTokenExpiredAt);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiredAt(accessTokenExpiredAt)
                .refreshTokenExpiredAt(refreshTokenExpiredAt)
                .build();
    }

    private Date createAccessTokenExpirationTime(long now) {
        return new Date(now + accessTokenValidityInMilliseconds);
    }

    private Date createRefreshTokenExpirationTime(long now) {
        return new Date(now + refreshTokenValidityInMilliseconds);
    }

    private String createRefreshToken(Date expireDate) {
        return Jwts.builder()
                .setIssuer(issuer)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createAccessToken(Authentication authentication, Date expireDate) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim("auth", authorities)
                .setIssuer(issuer)
                .setExpiration(expireDate)
                .setSubject(authentication.getName())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}