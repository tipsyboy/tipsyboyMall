package study.tipsyboy.tipsyboyMall.cart.dto;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;

@Getter
public class CartItemResponseDto {

    private Long cartItemId;

    private String itemName;

    private Integer price;

    private Integer count;

    public CartItemResponseDto(CartItem entity) {
        this.cartItemId = entity.getId();
        this.itemName = entity.getItem().getItemName();
        this.price = entity.getItem().getPrice();
        this.count = entity.getCount();
    }
}
