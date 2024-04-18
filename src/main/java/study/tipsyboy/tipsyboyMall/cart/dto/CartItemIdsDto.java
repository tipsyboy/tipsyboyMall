package study.tipsyboy.tipsyboyMall.cart.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CartItemIdsDto {

    private List<Long> ids;

    public CartItemIdsDto(List<Long> ids) {
        this.ids = ids;
    }
}
