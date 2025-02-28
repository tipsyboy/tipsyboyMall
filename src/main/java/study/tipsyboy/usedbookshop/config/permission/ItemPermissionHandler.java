package study.tipsyboy.usedbookshop.config.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import study.tipsyboy.usedbookshop.auth.dto.LoginMember;
import study.tipsyboy.usedbookshop.item.domain.Item;
import study.tipsyboy.usedbookshop.item.repository.ItemRepository;
import study.tipsyboy.usedbookshop.item.exception.ItemException;
import study.tipsyboy.usedbookshop.item.exception.ItemExceptionType;

@Slf4j
@RequiredArgsConstructor
public class ItemPermissionHandler implements PermissionHandler {

    private final ItemRepository itemRepository;

    @Override
    public boolean supports(String targetType) {
        return targetType.equals("ITEM");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Long targetId, String permission) {
        log.info("[Item - Permission Handler]");

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Item item = itemRepository.findById((Long) targetId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));


        if (!item.getMemberId().equals(loginMember.getMemberId())) {
            log.error("[인가 실패] 해당 상품에 대한 권한이 없습니다. itemId={}", item.getId());
            return false;
        }

        return true;
    }
}
