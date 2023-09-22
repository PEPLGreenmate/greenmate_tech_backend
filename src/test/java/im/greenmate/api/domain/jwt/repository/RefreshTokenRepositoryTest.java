package im.greenmate.api.domain.jwt.repository;

import im.greenmate.api.domain.jwt.entity.RefreshToken;
import im.greenmate.api.global.config.LocalRedisConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

@Import(LocalRedisConfig.class)
@DataRedisTest
@ActiveProfiles("dev")
class RefreshTokenRepositoryTest {

    private static RefreshTokenRepository refreshTokenRepository;

    @BeforeAll
    static void setUp(@Autowired RedisTemplate redisTemplate) {
        refreshTokenRepository = new RefreshTokenRepository(redisTemplate);
    }

    @BeforeEach
    void cleanUp() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    void CRUDTest() {
        //given
        String refreshToken = "refreshToken";
        String username = "tester123";

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 5);
        Date expiredAt = cal.getTime();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .refreshToken(refreshToken)
                .username(username)
                .expiredAt(expiredAt)
                .build();

        //when
        refreshTokenRepository.save(refreshTokenObject);

        // then
        assertThat(refreshTokenRepository.existsByKey(refreshToken)).isTrue();
        assertThat(refreshTokenRepository.existsByKey("NotFoundToken")).isFalse();
    }

    @Test
    void notSaveDataThatHasExpiredDate() {
        //given
        String refreshToken = "refreshToken";
        String username = "tester123";

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Date expiredAt = cal.getTime();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .refreshToken(refreshToken)
                .username(username)
                .expiredAt(expiredAt)
                .build();

        //when
        refreshTokenRepository.save(refreshTokenObject);

        // then
        assertThat(refreshTokenRepository.existsByKey(refreshToken)).isFalse();
    }

    @Test
    void deleteDataThatHasExpiredDate() throws InterruptedException {
        //given
        String refreshToken = "refreshToken";
        String username = "tester123";

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MILLISECOND, 300);
        Date expiredAt = cal.getTime();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .refreshToken(refreshToken)
                .username(username)
                .expiredAt(expiredAt)
                .build();

        //when
        refreshTokenRepository.save(refreshTokenObject);

        // then
        assertThat(refreshTokenRepository.existsByKey(refreshToken)).isTrue();
        sleep(300);
        assertThat(refreshTokenRepository.existsByKey(refreshToken)).isFalse();
    }
}