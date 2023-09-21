package im.greenmate.api.global.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile({"dev", "test"})
@Configuration
class LocalRedisConfig {
    private final RedisServer redisServer;

    public LocalRedisConfig(@Value("{spring.data.redis.port}") int redisPort) {
        this.redisServer = new RedisServer(redisPort);
    }

    @PostConstruct
    private void startRedis() {
        redisServer.start();
    }

    @PreDestroy
    private void stopRedis() {
        redisServer.stop();
    }
}
