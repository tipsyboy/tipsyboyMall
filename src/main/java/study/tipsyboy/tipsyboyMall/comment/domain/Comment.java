package study.tipsyboy.tipsyboyMall.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.common.domain.BaseTimeEntity;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // 부모 댓글

    @Column(nullable = false)
    private String content; // 댓글 내용

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Comment(Member author, Item item, Comment parentComment, String content) {
        mappingItem(item);
        this.author = author;
        this.parentComment = parentComment;
        this.content = content;
        this.isDeleted = false;
    }

    public void editContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.isDeleted = true;
    }

    private void mappingItem(Item item) {
        item.getComments().add(this);
        this.item = item;
    }
}
