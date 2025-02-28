package study.tipsyboy.usedbookshop.auth.domain;

import lombok.Getter;

@Getter
public enum MemberRole {

    GUEST("ROLE_GUEST", "손님"),
    MEMBER("ROLE_MEMBER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String role;
    private final String title;

    MemberRole(String role, String title) {
        this.role = role;
        this.title = title;
    }
}
