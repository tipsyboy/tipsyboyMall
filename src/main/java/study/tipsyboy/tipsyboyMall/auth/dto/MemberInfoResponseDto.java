package study.tipsyboy.tipsyboyMall.auth.dto;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;

@Getter
public class MemberInfoResponseDto {

    private Long id;

    private String email;

    private String nickname;

    private String role;

    public static MemberInfoResponseDto of(Member entity) {
        MemberInfoResponseDto dto = new MemberInfoResponseDto();
        dto.id = entity.getId();
        dto.email = entity.getEmail();
        dto.nickname = entity.getNickname();
        dto.role = entity.getMemberRole().getRole();
        return dto;
    }

}
