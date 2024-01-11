package study.tipsyboy.tipsyboyMall.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByMemberIdAndItemId(Long memberId, Long itemId);

    List<CartItem> findByMemberId(Long memberId);
}
