package study.tipsyboy.usedbookshop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import study.tipsyboy.usedbookshop.cart.repository.CartItemRepository;
import study.tipsyboy.usedbookshop.comment.repository.CommentRepository;
import study.tipsyboy.usedbookshop.config.permission.*;
import study.tipsyboy.usedbookshop.item.repository.ItemRepository;
import study.tipsyboy.usedbookshop.likeitem.repository.LikeItemRepository;
import study.tipsyboy.usedbookshop.order.repository.OrderRepository;

import java.util.List;

@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
public class MethodSecurityConfig {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final LikeItemRepository likeItemRepository;
    private final CommentRepository commentRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        GlobalPermissionEvaluator evaluator = new GlobalPermissionEvaluator(
                List.of(
                        orderPermissionHandler(),
                        itemPermissionHandler(),
                        cartItemPermissionHandler(),
                        likeItemPermissionHandler(),
                        commentPermissionHandler()
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

    @Bean
    public PermissionHandler likeItemPermissionHandler() {
        return new LikeItemPermissionHandler(likeItemRepository);
    }

    @Bean
    public PermissionHandler commentPermissionHandler() {
        return new CommentPermissionHandler(commentRepository);
    }
}
