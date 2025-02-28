package study.tipsyboy.usedbookshop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.cart.domain.CartItem;
import study.tipsyboy.usedbookshop.item.domain.Item;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {

    Optional<CartItem> findByMemberAndItem(Member member, Item item);

    List<CartItem> findByMemberId(Long memberId);

    List<CartItem> findByIdIn(List<Long> ids);
}
