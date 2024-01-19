package study.tipsyboy.tipsyboyMall.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    private final Long itemId;
    private final Long parentCommentId;
    private final String content;

    @Builder
    public CommentCreateRequestDto(Long itemId, Long parentCommentId, String content) {
        this.itemId = itemId;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }
}
