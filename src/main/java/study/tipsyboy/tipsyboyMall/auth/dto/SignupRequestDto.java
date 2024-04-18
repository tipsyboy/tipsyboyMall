package study.tipsyboy.tipsyboyMall.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank
    private final String email;

    @NotBlank
    private final String nickname;

    @NotBlank
    private final String password;

    @Builder
    public SignupRequestDto(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
