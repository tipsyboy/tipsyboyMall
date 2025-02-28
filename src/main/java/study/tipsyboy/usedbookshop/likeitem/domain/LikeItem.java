package study.tipsyboy.usedbookshop.likeitem.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.common.domain.BaseTimeEntity;
import study.tipsyboy.usedbookshop.item.domain.Item;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class LikeItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public LikeItem(Member member, Item item) {
        this.member = member;
        this.item = item;
    }
}
