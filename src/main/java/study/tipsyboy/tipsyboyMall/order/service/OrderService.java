package study.tipsyboy.tipsyboyMall.order.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.domain.ItemRepository;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.OrderItem;
import study.tipsyboy.tipsyboyMall.order.domain.OrderRepository;
import study.tipsyboy.tipsyboyMall.order.domain.OrderStatus;
import study.tipsyboy.tipsyboyMall.order.dto.OrderCreateDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderInfoResponseDto;
import study.tipsyboy.tipsyboyMall.order.exception.OrderException;
import study.tipsyboy.tipsyboyMall.order.exception.OrderExceptionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderInfoResponseDto order(OrderCreateDto orderCreateDto) {
        List<OrderItem> orderItems = orderCreateDto.getOrderInfo().entrySet().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());
        Order order = Order.builder()
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

    private OrderItem createOrderItem(Map.Entry<Long, Integer> info) {
        Long itemId = info.getKey();
        Integer count = info.getValue();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        return OrderItem.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice())
                .build();
    }
}
