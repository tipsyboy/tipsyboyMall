package study.tipsyboy.usedbookshop.order.dto;

import lombok.Getter;
import study.tipsyboy.usedbookshop.order.domain.OrderItem;

@Getter
public class OrderItemResponseDto {

    private Long itemId; // TODO: 이거 있어야하나?

    private String itemName;

    private Integer orderPrice;

    private Integer count;

    private String itemThumbnailImage;

    public static OrderItemResponseDto of(OrderItem entity) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.itemId = entity.getItemId();
        dto.itemName = entity.getItem().getItemName();
        dto.orderPrice = entity.getOrderPrice();
        dto.count = entity.getCount();
        dto.itemThumbnailImage
                = entity.getItem().getItemImages().isEmpty() ? null : entity.getItem().getItemImages().get(0).getStoredName();
        return dto;
    }
}