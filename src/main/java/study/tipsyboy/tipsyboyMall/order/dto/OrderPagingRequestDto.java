package study.tipsyboy.tipsyboyMall.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderPagingRequestDto {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 20;

    public Long getOffset() {
        return (long) (Math.max(1, this.page) - 1) * Math.min(this.size, MAX_SIZE);
    }
}
