package study.tipsyboy.tipsyboyMall.auth.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.tipsyboy.tipsyboyMall.auth.dto.SignupRequestDto;
import study.tipsyboy.tipsyboyMall.auth.service.AuthService;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/signup")
    public Long signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return authService.signup(signupRequestDto);
    }

}
