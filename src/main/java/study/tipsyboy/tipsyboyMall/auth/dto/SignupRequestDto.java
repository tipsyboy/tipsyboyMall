package study.tipsyboy.tipsyboyMall.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    private final String email;

    private final String nickname;

    private final String password;

    @Builder
    public SignupRequestDto(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
