package study.tipsyboy.tipsyboyMall.order.dto;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.order.domain.OrderItem;

@Getter
public class OrderItemResponseDto {

    private Long itemId;

    private Integer orderPrice;

    private Integer count;

    public static OrderItemResponseDto of(OrderItem entity) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.itemId = entity.getItemId();
        dto.orderPrice = entity.getOrderPrice();
        dto.count = entity.getCount();
        return dto;
    }
}