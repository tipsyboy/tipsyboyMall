package study.tipsyboy.usedbookshop.cart.repository;

import org.springframework.data.domain.Page;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.cart.domain.CartItem;
import study.tipsyboy.usedbookshop.cart.dto.CartItemSearchReqDto;

public interface CartItemRepositoryCustom {

    Page<CartItem> getCartItemList(CartItemSearchReqDto requestDto, Member member);
}
