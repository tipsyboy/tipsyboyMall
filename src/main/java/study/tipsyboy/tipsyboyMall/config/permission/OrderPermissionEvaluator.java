package study.tipsyboy.tipsyboyMall.config.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.OrderRepository;
import study.tipsyboy.tipsyboyMall.order.exception.OrderException;
import study.tipsyboy.tipsyboyMall.order.exception.OrderExceptionType;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class OrderPermissionEvaluator implements PermissionEvaluator {

    private final OrderRepository orderRepository;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        log.info("[Order - Permission Evaluator]");

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Order order = orderRepository.findById((Long) targetId)
                .orElseThrow(() -> new OrderException(OrderExceptionType.ORDER_NOT_FOUND));

        if (!order.getMemberId().equals(loginMember.getMemberId())) {
            log.error("[인가 실패] 해당 주문의 권한이 없습니다. orderId={}", order.getId());
            return false;
        }

        return true;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }
}
