package im.greenmate.api.domain.user.service;

import im.greenmate.api.domain.user.dto.request.SignupRequest;
import im.greenmate.api.domain.user.entity.Role;
import im.greenmate.api.domain.user.entity.User;
import im.greenmate.api.domain.user.exception.DuplicationNicknameException;
import im.greenmate.api.domain.user.exception.UserAlreadyExistsException;
import im.greenmate.api.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSignupServiceTest {
    @InjectMocks
    private UserSignupService userSignupService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() {
        // given
        SignupRequest request = new SignupRequest("username", "password", "nickname");

        // when
        userSignupService.signup(request);

        // then
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(argument.capture());
        assertThat(argument.getValue().getUsername()).isEqualTo(request.getUsername());
        assertThat(argument.getValue().getNickname()).isEqualTo(request.getNickname());
        assertThat(argument.getValue().getRole()).isEqualTo(Role.ROLE_USER);
        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), argument.getValue().getPassword());
        assertThat(isPasswordMatch).isTrue();
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 닉네임")
    void signupFailByDuplicateNickname() {
        // given
        SignupRequest request = new SignupRequest("username", "password", "nickname");
        when(userRepository.existsByNickname(request.getNickname())).thenReturn(true);

        // expected
        assertThrows(DuplicationNicknameException.class,
                () -> userSignupService.signup(request));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 유저네임")
    void signupFailByDuplicateUsername() {
        // given
        SignupRequest request = new SignupRequest("username", "password", "nickname");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // expected
        assertThrows(UserAlreadyExistsException.class,
                () -> userSignupService.signup(request));
    }
}