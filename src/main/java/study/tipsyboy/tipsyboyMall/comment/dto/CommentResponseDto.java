package study.tipsyboy.tipsyboyMall.comment.dto;

import lombok.Builder;
import lombok.Getter;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String author;
    private final String content;

    private final Long parentCommentId;

    private final LocalDateTime createDate;

    @Builder
    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.author = entity.getAuthor().getNickname();
        this.content = entity.getContent();
        this.parentCommentId = entity.getParentComment() == null ? null : entity.getParentComment().getId();
        this.createDate = entity.getCreateDate();
    }
}
