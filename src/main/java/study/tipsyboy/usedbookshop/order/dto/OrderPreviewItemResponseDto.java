package study.tipsyboy.usedbookshop.order.dto;

import lombok.Getter;
import study.tipsyboy.usedbookshop.cart.domain.CartItem;

@Getter
public class OrderPreviewItemResponseDto {

    private Long cartItemId;

    private Long itemId;

    private String itemName;

    private String itemThumnailImage;

    private Integer price;

    private Integer count;

    private Integer stock;

    public OrderPreviewItemResponseDto(CartItem cartItem) {
        this.itemName = cartItem.getItem().getItemName();
        this.price = cartItem.getItem().getPrice();
        this.stock = cartItem.getItem().getStock();
    }
}
