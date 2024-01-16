package study.tipsyboy.tipsyboyMall.likeitem.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.likeitem.dto.LikeItemResponseDto;
import study.tipsyboy.tipsyboyMall.likeitem.service.LikeItemService;

@RequestMapping("/api/likeitem")
@RequiredArgsConstructor
@RestController
public class LikeItemApiController {

    private final LikeItemService likeItemService;

    @PostMapping("/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')")
    public ResponseEntity<Long> addLikeItem(@PathVariable Long itemId,
                                            @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(likeItemService.addLikeItem(loginMember.getMemberId(), itemId));
    }

    @GetMapping
    public ResponseEntity<Page<LikeItemResponseDto>> readLikeItems(@AuthenticationPrincipal LoginMember loginMember,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page) {
        return ResponseEntity.ok(likeItemService.readLikeItem(loginMember.getMemberId(), page));
    }

    @DeleteMapping("/{likeItemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#likeItemId, 'LIKE_ITEM', 'DELETE'))")
    public ResponseEntity<Void> deleteLikeItem(@PathVariable Long likeItemId) {
        likeItemService.deleteLikeItem(likeItemId);
        return ResponseEntity.ok().build();
    }
}
