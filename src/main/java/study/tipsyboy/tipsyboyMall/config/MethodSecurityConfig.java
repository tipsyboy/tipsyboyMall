package study.tipsyboy.tipsyboyMall.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.config.permission.*;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.order.repository.OrderRepository;

import java.util.List;

@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
public class MethodSecurityConfig {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        GlobalPermissionEvaluator evaluator = new GlobalPermissionEvaluator(
                List.of(
                        orderPermissionHandler(),
                        itemPermissionHandler(),
                        cartItemPermissionHandler()
                ));

        handler.setPermissionEvaluator(evaluator);
        return handler;
    }

    @Bean
    public PermissionHandler orderPermissionHandler() {
        return new OrderPermissionHandler(orderRepository);
    }

    @Bean
    public PermissionHandler itemPermissionHandler() {
        return new ItemPermissionHandler(itemRepository);
    }

    @Bean
    public PermissionHandler cartItemPermissionHandler() {
        return new CartItemPermissionHandler(cartItemRepository);
    }
}
