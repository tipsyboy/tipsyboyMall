package study.tipsyboy.tipsyboyMall.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CartItemUpdateRequestDto {

    private Long itemId;

    private Integer count; // 수량

    @Builder
    public CartItemUpdateRequestDto(Long itemId, Integer count) {
        this.itemId = itemId;
        this.count = count;
    }
}
