package im.greenmate.api.global.security;

import im.greenmate.api.domain.user.entity.User;
import im.greenmate.api.domain.user.exception.UserNotFoundException;
import im.greenmate.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Account account = Account.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        String roleName = user.getRole().toString();
        List<String> role = Arrays.stream(roleName.split(",")).toList();

        return new UserDetailsImpl(account, role);
    }
}
