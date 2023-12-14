package study.tipsyboy.tipsyboyMall.auth.dto;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;

import java.util.List;

@Getter
public class LoginMember extends User {

    private final Long memberId;
    private final String nickname;


    public LoginMember(Member member) {
        super(member.getEmail(), member.getPassword(),
                List.of(new SimpleGrantedAuthority(
                        member.getMemberRole().getRole()
                )));
        this.memberId = member.getId();
        this.nickname = member.getNickname();
    }
}