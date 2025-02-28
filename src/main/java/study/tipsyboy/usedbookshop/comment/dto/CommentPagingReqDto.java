package study.tipsyboy.usedbookshop.comment.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Builder
@Getter
public class CommentPagingReqDto {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 20;

    public Integer getPage() {
        return Integer.min(1, this.page);
    }

    public Long getOffset() {
        return (long) (Math.max(1, this.page) - 1) * Math.max(this.size, MAX_SIZE);
    }

    public Pageable getPageable() {
        return PageRequest.of(this.page - 1, this.size);
    }
}
