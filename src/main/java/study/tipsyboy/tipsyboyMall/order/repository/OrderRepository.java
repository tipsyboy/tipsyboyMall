package study.tipsyboy.tipsyboyMall.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.tipsyboyMall.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
