package study.tipsyboy.usedbookshop.config.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import study.tipsyboy.usedbookshop.auth.dto.LoginMember;
import study.tipsyboy.usedbookshop.order.domain.Order;
import study.tipsyboy.usedbookshop.order.repository.OrderRepository;
import study.tipsyboy.usedbookshop.order.exception.OrderException;
import study.tipsyboy.usedbookshop.order.exception.OrderExceptionType;

@Slf4j
@RequiredArgsConstructor
public class OrderPermissionHandler implements PermissionHandler {

    private final OrderRepository orderRepository;

    @Override
    public boolean supports(String targetType) {
        return targetType.equals("ORDER");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Long targetId, String permission) {
        log.info("[Order - Permission Handler]");

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Order order = orderRepository.findById((Long) targetId)
                .orElseThrow(() -> new OrderException(OrderExceptionType.ORDER_NOT_FOUND));

        if (!order.getMemberId().equals(loginMember.getMemberId())) {
            log.error("[인가 실패] 해당 주문의 권한이 없습니다. orderId={}", order.getId());
            return false;
        }

        return true;
    }
}
