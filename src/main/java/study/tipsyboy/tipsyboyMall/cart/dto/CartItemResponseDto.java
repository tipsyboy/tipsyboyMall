package study.tipsyboy.tipsyboyMall.cart.dto;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;

@Getter
public class CartItemResponseDto {
    private Long cartItemId;

    private Long itemId;

    private String itemName;

    private String itemThumbnailImage;

    private Integer price;

    private Integer count;

    private Integer stock;


    public CartItemResponseDto(CartItem entity) {
        this.cartItemId = entity.getId();
        this.itemId = entity.getItem().getId();
        this.itemName = entity.getItem().getItemName();
        this.itemThumbnailImage
                = entity.getItem().getItemImages().isEmpty() ? null : entity.getItem().getItemImages().get(0).getStoredName();
        this.price = entity.getItem().getPrice();
        this.count = entity.getCount();
        this.stock = entity.getItem().getStock();
    }
}
