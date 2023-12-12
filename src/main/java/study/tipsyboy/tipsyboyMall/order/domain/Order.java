package study.tipsyboy.tipsyboyMall.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.tipsyboy.tipsyboyMall.common.domain.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(OrderStatus orderStatus, List<OrderItem> orderItems) {
        this.orderStatus = orderStatus;
        for (OrderItem orderItem : orderItems) {
            this.addOrderItems(orderItem);
        }
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCEL;
        this.orderItems
                .forEach(orderItem -> orderItem.cancel());
    }

    private void addOrderItems(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.connectOrder(this);
    }
}
