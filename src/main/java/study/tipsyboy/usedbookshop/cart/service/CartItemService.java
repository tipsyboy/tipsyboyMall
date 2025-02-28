package study.tipsyboy.usedbookshop.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.auth.domain.MemberRepository;
import study.tipsyboy.usedbookshop.auth.exception.AuthException;
import study.tipsyboy.usedbookshop.auth.exception.AuthExceptionType;
import study.tipsyboy.usedbookshop.cart.domain.CartItem;
import study.tipsyboy.usedbookshop.cart.dto.*;
import study.tipsyboy.usedbookshop.cart.repository.CartItemRepository;
import study.tipsyboy.usedbookshop.item.domain.Item;
import study.tipsyboy.usedbookshop.item.exception.ItemException;
import study.tipsyboy.usedbookshop.item.exception.ItemExceptionType;
import study.tipsyboy.usedbookshop.item.repository.ItemRepository;
import study.tipsyboy.usedbookshop.response.PagingResponse;

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

    public PagingResponse<CartItemResponseDto> readCartItems(Long memberId, CartItemSearchReqDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        Page<CartItem> cartItemList = cartItemRepository.getCartItemList(requestDto, member);

        return new PagingResponse<>(cartItemList, CartItemResponseDto.class);
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
