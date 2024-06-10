package study.tipsyboy.tipsyboyMall.cart.repository;

import org.springframework.data.domain.Page;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemSearchReqDto;

public interface CartItemRepositoryCustom {

    Page<CartItem> getCartItemList(CartItemSearchReqDto requestDto, Member member);
}
