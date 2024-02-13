package study.tipsyboy.tipsyboyMall.order.dto;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;

@Getter
public class OrderPreviewItemResponseDto {

    private final String itemName;
    private final Integer price;
    private final Integer stock;

    public OrderPreviewItemResponseDto(CartItem cartItem) {
        this.itemName = cartItem.getItem().getItemName();
        this.price = cartItem.getItem().getPrice();
        this.stock = cartItem.getItem().getStock();
    }
}
