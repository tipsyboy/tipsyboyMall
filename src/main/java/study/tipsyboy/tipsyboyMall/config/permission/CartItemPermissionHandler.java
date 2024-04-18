package study.tipsyboy.tipsyboyMall.config.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;

@Slf4j
@RequiredArgsConstructor
public class CartItemPermissionHandler implements PermissionHandler {

    private final CartItemRepository cartItemRepository;

    @Override
    public boolean supports(String targetType) {
        return targetType.equals("CART_ITEM");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Long targetId, String permission) {
        log.info("[CartItem - Permission Handler]");

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        CartItem cartItem = cartItemRepository.findById(targetId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        if (!cartItem.getMember().getId().equals(loginMember.getMemberId())) {
            log.error("[인가 실패] 장바구니 상품에 대한 권한이 없습니다.");
            return false;
        }
        return true;
    }
}
