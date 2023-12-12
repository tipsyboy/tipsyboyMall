package study.tipsyboy.tipsyboyMall.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.tipsyboyMall.order.domain.Order;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();

    @Builder
    public Member(String email, String nickname, String password, MemberRole memberRole) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.memberRole = memberRole;
    }
}
