package study.tipsyboy.tipsyboyMall.order.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthException;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthExceptionType;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.OrderItem;
import study.tipsyboy.tipsyboyMall.order.dto.OrderPagingRequestDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderPreviewItemResponseDto;
import study.tipsyboy.tipsyboyMall.order.repository.OrderRepository;
import study.tipsyboy.tipsyboyMall.order.domain.OrderStatus;
import study.tipsyboy.tipsyboyMall.order.dto.OrderByCartCreateDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderInfoResponseDto;
import study.tipsyboy.tipsyboyMall.order.exception.OrderException;
import study.tipsyboy.tipsyboyMall.order.exception.OrderExceptionType;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderInfoResponseDto order(Long memberId, OrderByCartCreateDto orderByCartCreateDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));


        List<OrderItem> orderItems = orderByCartCreateDto.getCartItemIds().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());

        Order order = Order.builder()
                .member(member)
                .orderItems(orderItems)
                .orderStatus(OrderStatus.ORDER)
                .build();
        orderRepository.save(order);

        return new OrderInfoResponseDto(order);
    }

    public OrderInfoResponseDto findOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderExceptionType.ORDER_NOT_FOUND));

        return new OrderInfoResponseDto(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderExceptionType.ORDER_NOT_FOUND));

        order.cancel();
    }

    public List<OrderInfoResponseDto> findOrderListByMemberId(
            OrderPagingRequestDto pagingRequestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        return orderRepository.getOrderListByMemberId(pagingRequestDto, member).stream()
                .map(OrderInfoResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<OrderPreviewItemResponseDto> preview(List<Long> selectedCartItems) {
        return selectedCartItems.stream()
                .map(
                        cartItemId -> new OrderPreviewItemResponseDto(
                                cartItemRepository.findById(cartItemId)
                                        .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND))
                        )
                )
                .collect(Collectors.toList());
    }

    private OrderItem createOrderItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        OrderItem orderItem = OrderItem.builder()
                .item(cartItem.getItem())
                .count(cartItem.getCount())
                .orderPrice(cartItem.getItem().getPrice())
                .build();

        cartItemRepository.delete(cartItem);

        return orderItem;
    }
}
