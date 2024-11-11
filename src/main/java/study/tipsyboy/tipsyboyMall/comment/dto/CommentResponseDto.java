package study.tipsyboy.tipsyboyMall.comment.dto;

import lombok.Builder;
import lombok.Getter;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponseDto {

    private final Long commentId;

    private final Long itemId;

    private final String author;

    private final String content;

    private final Long parentCommentId;

    private final List<CommentResponseDto> children = new ArrayList<>();

    private final LocalDateTime createdDate;

    @Builder
    public CommentResponseDto(Comment entity) {
        this.commentId = entity.getId();
        this.itemId = entity.getItem().getId();
        this.author = entity.getAuthor().getNickname();
        this.content = entity.getContent();
        this.parentCommentId = entity.getParentComment() == null ? null : entity.getParentComment().getId();
        this.createdDate = entity.getCreateDate();

        for (Comment child : entity.getChildren()) {
            this.children.add(new CommentResponseDto(child));
        }
    }
}
