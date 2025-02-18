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
    private DeliveryRequestDto delivery;

    @Builder
    public OrderByCartCreateDto(List<Long> cartItemIds, DeliveryRequestDto delivery) {
        this.cartItemIds = cartItemIds;
        this.delivery = delivery;
    }
}
