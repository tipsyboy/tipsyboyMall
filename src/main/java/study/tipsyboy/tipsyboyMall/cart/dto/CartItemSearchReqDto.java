package study.tipsyboy.tipsyboyMall.cart.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@NoArgsConstructor
@AllArgsConstructor
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

    public Pageable getPageable() {
        return PageRequest.of(this.page - 1, this.size);
    }

    public Integer getPage() {
        return this.page != null ? this.page : 1;
    }

    public Integer getSize() {
        return this.size != null ? Math.min(this.size, MAX_SIZE) : 20;
    }
}
