package study.tipsyboy.tipsyboyMall.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentContentEditRequestDto {

    private final Long commentId;
    private final String content;

    @Builder
    public CommentContentEditRequestDto(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
