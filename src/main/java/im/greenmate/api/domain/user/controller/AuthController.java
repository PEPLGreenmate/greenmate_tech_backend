package im.greenmate.api.domain.user.controller;

import im.greenmate.api.domain.user.dto.request.LoginRequest;
import im.greenmate.api.domain.user.dto.request.LogoutRequest;
import im.greenmate.api.domain.user.dto.request.SignupRequest;
import im.greenmate.api.domain.user.dto.request.TokenReissueRequest;
import im.greenmate.api.domain.user.dto.response.LoginResponse;
import im.greenmate.api.domain.user.dto.response.TokenReissueResponse;
import im.greenmate.api.domain.user.service.AuthReissueService;
import im.greenmate.api.domain.user.service.UserLoginService;
import im.greenmate.api.domain.user.service.UserLogoutService;
import im.greenmate.api.domain.user.service.UserSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final UserLogoutService userLogoutService;
    private final AuthReissueService authReissueService;

    @PostMapping("signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {
        userSignupService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userLoginService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("reissue")
    public ResponseEntity<TokenReissueResponse> reissue(@RequestBody TokenReissueRequest request) {
        TokenReissueResponse response = authReissueService.reissue(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        userLogoutService.logout(request);
        return ResponseEntity.ok().build();
    }
}
