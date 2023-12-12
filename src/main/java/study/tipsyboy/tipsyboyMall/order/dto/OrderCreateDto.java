package study.tipsyboy.tipsyboyMall.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OrderCreateDto {

    private Map<Long, Integer> orderInfo;

    @Builder
    public OrderCreateDto(Map<Long, Integer> orderInfo) {
        this.orderInfo = orderInfo;
    }

    protected OrderCreateDto() {
    }
}
