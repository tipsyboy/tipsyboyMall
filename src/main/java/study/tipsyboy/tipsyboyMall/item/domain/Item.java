package study.tipsyboy.tipsyboyMall.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;
import study.tipsyboy.tipsyboyMall.common.domain.BaseTimeEntity;
import study.tipsyboy.tipsyboyMall.files.ItemFile;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.likeitem.domain.LikeItem;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;
    private int price;
    private int stock; // 재고
    private String description; // 상품

    private int likes;

    @Enumerated(value = EnumType.STRING)
    private ItemStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 판매자

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemFile> itemImages = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<LikeItem> likeItemList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Item(String itemName, int price, int stock, String description, Member member) {
        setMember(member);
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.status = ItemStatus.ON_SALE;
        this.likes = 0;
    }

    public ItemEditor.ItemEditorBuilder toEditor() {
        return ItemEditor.builder()
                .itemName(this.itemName)
                .price(this.price)
                .stock(this.stock)
                .description(this.description);
    }

    public void update(ItemEditor itemEditor) {
        this.itemName = itemEditor.getItemName();
        this.price = itemEditor.getPrice();
        this.stock = itemEditor.getStock();
        this.description = itemEditor.getDescription();
    }

    public Long getMemberId() {
        return this.member.getId();
    }

    public void addStock(int count) {
        this.stock += count;
    }

    public void removeStock(int count) {
        if (this.stock < count) {
            throw new ItemException(ItemExceptionType.ITEM_NOT_ENOUGH);
        }
        this.stock -= count;
    }

    private void setMember(Member member) {
        this.member = member;
        member.getItemList().add(this);
    }

    public void addLikeCount() {
        this.likes += 1;
    }

}
