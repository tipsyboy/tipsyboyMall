package study.tipsyboy.usedbookshop.order.dto;

import lombok.Getter;
import study.tipsyboy.usedbookshop.order.domain.Order;
import study.tipsyboy.usedbookshop.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderInfoResponseDto {

    private final Long id;

    private final OrderStatus orderStatus;

    private final LocalDateTime orderedDate;

    private final List<OrderItemResponseDto> orderItemList;

    private final DeliveryResponseDto orderInfo;

    // 총 금액

    public OrderInfoResponseDto(Order entity) {
        this.id = entity.getId();
        this.orderStatus = entity.getOrderStatus();
        this.orderedDate = entity.getCreateDate();
        this.orderItemList = entity.getOrderItems().stream()
                .map(OrderItemResponseDto::of)
                .collect(Collectors.toList());
        this.orderInfo = DeliveryResponseDto.of(entity.getDelivery());
    }
}