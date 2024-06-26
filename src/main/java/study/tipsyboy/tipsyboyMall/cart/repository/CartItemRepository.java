package study.tipsyboy.tipsyboyMall.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {

    Optional<CartItem> findByMemberAndItem(Member member, Item item);

    List<CartItem> findByMemberId(Long memberId);

    List<CartItem> findByIdIn(List<Long> ids);
}
