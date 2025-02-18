package study.tipsyboy.tipsyboyMall.comment.dto;

import lombok.Builder;
import lombok.Getter;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final Long itemId;

    private final String author;
    private final String content;
    private final LocalDateTime createdDate;

    private final Long parentCommentId;
    private final String parentCommentAuthor;

    private final List<CommentResponseDto> children = new ArrayList<>();

    private final boolean deleted;

    private final int likeCnt;
    private final int dislikeCnt;

    @Builder
    public CommentResponseDto(Comment entity) {
        this.commentId = entity.getId();
        this.itemId = entity.getItem().getId();
        this.author = entity.getAuthor().getNickname();
        this.deleted = entity.isDeleted();
        this.content = entity.isDeleted() ? "삭제된 댓글입니다." : entity.getContent();
        this.likeCnt = entity.getLikeCnt();
        this.dislikeCnt = entity.getDislikeCnt();

        if (entity.getParentComment() != null) {
            this.parentCommentId = entity.getParentComment().getId();
            this.parentCommentAuthor = entity.getParentComment().getAuthor().getNickname();
        } else {
            this.parentCommentId = null;
            this.parentCommentAuthor = null;
        }

        this.createdDate = entity.getCreateDate();
        for (Comment child : entity.getChildren()) {
            this.children.add(new CommentResponseDto(child));
        }
    }
}
