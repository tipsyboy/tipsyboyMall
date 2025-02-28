package study.tipsyboy.usedbookshop.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagingResponse<T> {

    private final long page;
    private final long size;
    private final long pageSize;
    private final long totalCount;
    private final List<T> contents;

    public PagingResponse(Page<?> page, Class<T> clazz) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.pageSize = page.getTotalPages();
        this.totalCount = page.getTotalElements();
        this.contents = page.getContent().stream()
                .map(content -> {
                    try {
                        return clazz.getConstructor(content.getClass()).newInstance(content);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }
}
