package study.tipsyboy.usedbookshop.cart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.cart.domain.CartItem;
import study.tipsyboy.usedbookshop.cart.dto.CartItemSearchReqDto;

import java.util.List;

import static study.tipsyboy.usedbookshop.cart.domain.QCartItem.cartItem;

@RequiredArgsConstructor
public class CartItemRepositoryCustomImpl implements CartItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CartItem> getCartItemList(CartItemSearchReqDto requestDto, Member member) {
        List<CartItem> contents = jpaQueryFactory.selectFrom(cartItem)
                .where(
                        cartItem.member.eq(member)
                )
                .limit(requestDto.getSize())
                .offset(requestDto.getOffset())
                .orderBy(cartItem.id.desc())
                .fetch();

        Long total = jpaQueryFactory.select(cartItem.count())
                .from(cartItem)
                .where(
                        cartItem.member.eq(member)
                )
                .fetchOne();
        return new PageImpl<>(contents, requestDto.getPageable(), total == null ? 0 : total);
    }
}
