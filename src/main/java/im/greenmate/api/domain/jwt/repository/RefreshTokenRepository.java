package im.greenmate.api.domain.jwt.repository;

import im.greenmate.api.domain.jwt.entity.RefreshToken;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(RefreshToken refreshToken) {
        String key = refreshToken.getRefreshToken();
        String value = refreshToken.getUsername();
        long timeout = refreshToken.getExpiredAt().getTime() - System.currentTimeMillis();
        if (timeout > 0) {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
        }
    }

    public boolean existsByKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void deleteAll() {
        try {
            RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
            connection.serverCommands().flushAll();
        } catch (Exception e) {
            throw new RuntimeException("Redis clean up is error", e);
        }
    }
}