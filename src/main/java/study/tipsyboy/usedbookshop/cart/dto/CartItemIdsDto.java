package study.tipsyboy.usedbookshop.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CartItemIdsDto {

    private List<Long> ids;

    @Builder
    public CartItemIdsDto(List<Long> ids) {
        this.ids = ids;
    }
}
