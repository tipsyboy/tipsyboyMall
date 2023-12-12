package study.tipsyboy.tipsyboyMall.item.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.tipsyboyMall.common.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor
@Entity
public class Item extends BaseTimeEntity {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;
    private int price;
    private int stock; // 재고
    private String description; // 상품


    @Builder
    public Item(String itemName, int price, int stock, String description) {
        this.itemName = itemName;
        this.price = price;
        this.stock = stock;
        this.description = description;
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

    public void addStock(int count) {
        this.stock += count;
    }

    public void removeStock(int count) {
        if (this.stock < count) {
            throw new IllegalArgumentException("상품 재고가 없습니다.");
        }
        this.stock -= count;
    }
}
