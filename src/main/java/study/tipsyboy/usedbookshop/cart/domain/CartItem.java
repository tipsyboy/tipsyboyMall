package study.tipsyboy.usedbookshop.cart.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.item.domain.Item;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CartItem {

    @Id @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    @Builder
    public CartItem(Member member, Item item, int count) {
        this.member = member;
        this.item = item;
        this.count = count;
    }

    public void updateCount(Integer count) {
        if (count < 1) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        this.count = count;
    }
}
