package study.tipsyboy.tipsyboyMall.order.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderByCartCreateDto {

    private List<Long> cartItemIds;

    @Builder
    public OrderByCartCreateDto(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }
}
