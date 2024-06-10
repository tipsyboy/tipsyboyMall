package study.tipsyboy.tipsyboyMall.cart.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Builder
@Getter
public class CartItemSearchReqDto {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 20;

    // 검색 조건
    private String title;
    private String seller;

    public Long getOffset() {
        return (long) (Math.max(1, this.page) - 1) * Math.min(this.size, MAX_SIZE);
    }

    public Integer getPage() {
        return Math.max(1, this.page);
    }

    public Pageable getPageable() {
        return PageRequest.of(this.page - 1, this.size);
    }
}
