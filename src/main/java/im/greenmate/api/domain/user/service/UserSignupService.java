package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.user.dto.request.SignupRequest;
import im.greenmate.api.domain.user.entity.Role;
import im.greenmate.api.domain.user.entity.User;
import im.greenmate.api.domain.user.exception.DuplicationNicknameException;
import im.greenmate.api.domain.user.exception.UserAlreadyExistsException;
import im.greenmate.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest signupDto) {
        validateDuplicateNickname(signupDto.getNickname());
        validateExistUsername(signupDto.getUsername());

        User user = createUser(signupDto);
        userRepository.save(user);
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicationNicknameException();
        }
    }

    private void validateExistUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException();
        }
    }

    private User createUser(SignupRequest signupDto) {
        return User.builder()
                .role(Role.ROLE_USER)
                .nickname(signupDto.getNickname())
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .build();
    }
}