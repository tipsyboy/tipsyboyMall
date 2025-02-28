package study.tipsyboy.usedbookshop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.usedbookshop.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
}
