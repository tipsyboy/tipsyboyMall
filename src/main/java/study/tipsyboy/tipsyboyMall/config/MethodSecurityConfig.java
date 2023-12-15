package study.tipsyboy.tipsyboyMall.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import study.tipsyboy.tipsyboyMall.config.permission.OrderPermissionEvaluator;
import study.tipsyboy.tipsyboyMall.order.domain.OrderRepository;

@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
public class MethodSecurityConfig {

    private final OrderRepository orderRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new OrderPermissionEvaluator(orderRepository));

        return handler;
    }
}
