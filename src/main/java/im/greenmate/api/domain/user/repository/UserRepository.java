package im.greenmate.api.domain.user.repository;

import im.greenmate.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByUsername(String username);
}
