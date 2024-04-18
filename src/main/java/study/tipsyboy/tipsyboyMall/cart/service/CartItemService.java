package study.tipsyboy.tipsyboyMall.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthException;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthExceptionType;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemIdsDto;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemResponseDto;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemSaveRequestDto;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemUpdateRequestDto;
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Long save(Long memberId, CartItemSaveRequestDto requestDto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        // Think: 상품의 재고를 여기서 판단해야 하는가?
        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        Optional<CartItem> findCartItem = cartItemRepository.findByMemberAndItem(member, item);
        if (findCartItem.isPresent()) {
            findCartItem.get().updateCount(requestDto.getCount());
            return findCartItem.get().getId();
        }

        CartItem cartItem = CartItem.builder()
                .member(member)
                .item(item)
                .count(requestDto.getCount())
                .build();

        return cartItemRepository.save(cartItem).getId();
    }

    public List<CartItemResponseDto> readCartItems(Long memberId) {
        return cartItemRepository.findByMemberId(memberId).stream()
                .map(CartItemResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<CartItemResponseDto> readCartItemsByIds(List<Long> cartItemIds) {
        return cartItemRepository.findByIdIn(cartItemIds).stream()
                .map(CartItemResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void editCartItemCount(Long memberId, Long cartItemId, CartItemUpdateRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        cartItem.updateCount(requestDto.getCount());
    }

    @Transactional
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        cartItemRepository.delete(cartItem);
    }

}
