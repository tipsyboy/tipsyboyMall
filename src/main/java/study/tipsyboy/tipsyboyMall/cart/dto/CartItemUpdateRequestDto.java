package study.tipsyboy.tipsyboyMall.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItemUpdateRequestDto {

    private Long cartItemId;

    private Integer count; // 수량

    @Builder
    public CartItemUpdateRequestDto(Long cartItemId, Integer count) {
        this.cartItemId = cartItemId;
        this.count = count;
    }
}
