package study.tipsyboy.tipsyboyMall.order.domain;

import jakarta.persistence.*;
import lombok.*;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

@Getter
@ToString(exclude = "order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
    private int orderPrice;

    @Builder
    public OrderItem(Item item, int count, int orderPrice) {
        this.item = item;
        this.count = count;
        item.removeStock(count);
        this.orderPrice = orderPrice;
    }

    public void cancel() {
        this.getItem().addStock(this.count);
    }

    public void connectOrder(Order order) {
        this.order = order;
    }

    public Long getItemId() {
        return this.getItem().getId();
    }
}
