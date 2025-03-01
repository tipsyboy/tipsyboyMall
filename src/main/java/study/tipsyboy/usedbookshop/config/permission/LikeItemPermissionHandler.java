package study.tipsyboy.usedbookshop.config.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import study.tipsyboy.usedbookshop.auth.dto.LoginMember;
import study.tipsyboy.usedbookshop.item.exception.ItemException;
import study.tipsyboy.usedbookshop.item.exception.ItemExceptionType;
import study.tipsyboy.usedbookshop.likeitem.domain.LikeItem;
import study.tipsyboy.usedbookshop.likeitem.repository.LikeItemRepository;

@Slf4j
@RequiredArgsConstructor
public class LikeItemPermissionHandler implements PermissionHandler {

    private final LikeItemRepository likeItemRepository;

    @Override
    public boolean supports(String targetType) {
        return targetType.equals("LIKE_ITEM");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Long targetId, String permission) {
        log.info("[Like Item - Permission Handler]");

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        LikeItem likeItem = likeItemRepository.findById(targetId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        if (!likeItem.getMember().getId().equals(loginMember.getMemberId())) {
            log.error("[인가 실패] 해당 상품에 대한 권한이 없습니다. likeItemId={}", likeItem.getId());
            return false;
        }

        return true;
    }
}
