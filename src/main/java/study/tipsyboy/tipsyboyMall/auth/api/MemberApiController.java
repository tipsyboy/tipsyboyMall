package study.tipsyboy.tipsyboyMall.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.auth.dto.MemberInfoResponseDto;
import study.tipsyboy.tipsyboyMall.auth.service.MemberService;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    public ResponseEntity<MemberInfoResponseDto> getMe(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(memberService.findByMemberId(loginMember.getMemberId()));
    }

    @GetMapping("/profile/{nickname}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    public ResponseEntity<MemberInfoResponseDto> profile(@PathVariable String nickname) {
        return ResponseEntity.ok(memberService.findByNickname(nickname));
    }

}
