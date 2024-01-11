package study.tipsyboy.tipsyboyMall.cart.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemResponseDto;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemSaveRequestDto;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemUpdateRequestDto;
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CartItemServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @AfterEach
    private void after() {
        cartItemRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("상품을 장바구니에 등록한다.")
    public void saveCartItem() throws Exception {
        // given
        Member seller = Member.builder()
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member buyer = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);
        memberRepository.save(buyer);

        Item item = Item.builder()
                .itemName("토비의 스프링 vol1")
                .member(seller)
                .price(40000)
                .stock(10)
                .description("토비의 스프링 1권 - 스프링의 이해와 원리")
                .build();
        itemRepository.save(item);

        // when
        CartItemSaveRequestDto requestDto = CartItemSaveRequestDto.builder()
                .itemId(item.getId())
                .count(2)
                .build();
        cartItemService.save(buyer.getId(), requestDto);
        CartItem savedCartItem = cartItemRepository.findAll().get(0);

        // then
        assertEquals(1, cartItemRepository.count());
        assertEquals("토비의 스프링 vol1", savedCartItem.getItem().getItemName());
        assertEquals(40000, savedCartItem.getItem().getPrice());
        assertEquals("토비의 스프링 1권 - 스프링의 이해와 원리", savedCartItem.getItem().getDescription());

        assertEquals(item.getId(), savedCartItem.getItem().getId());
        assertEquals(2, savedCartItem.getCount());
    }

    @Test
    @DisplayName("사용자의 장바구니 상품을 조회한다.")
    public void getCartItemByMemberId() throws Exception {
        // given - 고객 2명이 상품을 주문한다.
        Member seller = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member buyer1 = Member.builder()
                .email("tipsyboy1@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member buyer2 = Member.builder()
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .nickname("만취맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);
        memberRepository.save(buyer1);
        memberRepository.save(buyer2);

        Item item1 = Item.builder()
                .itemName("페퍼로니 피자")
                .member(seller)
                .price(25900)
                .stock(10)
                .description("피자는 페퍼로니 피자가 근본이다.")
                .build();
        Item item2 = Item.builder()
                .itemName("포테이토 피자")
                .member(seller)
                .price(23900)
                .stock(10)
                .description("가끔 포테이토도 먹으면 맛있다.")
                .build();
        itemRepository.save(item1);
        itemRepository.save(item2);

        CartItem cartItem1 = CartItem.builder()
                .item(item1)
                .member(buyer1)
                .count(1)
                .build();
        CartItem cartItem2 = CartItem.builder()
                .item(item2)
                .member(buyer1)
                .count(1)
                .build();
        CartItem cartItem3 = CartItem.builder()
                .item(item1)
                .member(buyer2)
                .count(1)
                .build();
        cartItemRepository.save(cartItem1);
        cartItemRepository.save(cartItem2);
        cartItemRepository.save(cartItem3);

        // when - '간술맨'의 장바구니를 조회한다.
        List<CartItemResponseDto> responseDto = cartItemService.readCartItems(buyer1.getId());

        // then
        assertEquals(3, cartItemRepository.count()); // 전체 장바구니의 상품은 총 3개
        assertEquals(2, responseDto.size()); // '간술맨'의 장바구니에는 2개의 상품이 들어있다.

        assertEquals(item1.getItemName(), responseDto.get(0).getItemName());
        assertEquals(cartItem1.getCount(), responseDto.get(0).getCount());
        assertEquals(item2.getItemName(), responseDto.get(1).getItemName());
        assertEquals(cartItem2.getCount(), responseDto.get(1).getCount());
    }

    @Test
    @DisplayName("장바구니에 등록된 상품의 수량을 변경한다.")
    public void updateCartItemCount() throws Exception {
        // given
        Member seller = Member.builder()
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member buyer = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);
        memberRepository.save(buyer);

        Item item = Item.builder()
                .itemName("참치 김밥")
                .member(seller)
                .price(4500)
                .stock(10)
                .description("참치김밥 비싸다. 한 줄만 먹어야지")
                .build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .member(buyer)
                .count(1)
                .build();
        cartItemRepository.save(cartItem);

        // when
        CartItemUpdateRequestDto requestDto = CartItemUpdateRequestDto.builder()
                .itemId(item.getId())
                .count(2)
                .build();
        cartItemService.editCartItemCount(buyer.getId(), cartItem.getId(), requestDto);

        CartItem updatedCartItem = cartItemRepository.findAll().get(0);
        // then
        assertEquals(1, cartItemRepository.count());
        assertEquals(2, updatedCartItem.getCount());
    }

    @Test
    @DisplayName("장바구니에 등록된 상품을 삭제한다.")
    public void deleteCartItem() throws Exception {
        // given
        Member seller = Member.builder()
                .email("tipsyboy2@gmail.com")
                .password("1234")
                .nickname("혼술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        Member buyer = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);
        memberRepository.save(buyer);

        Item item = Item.builder()
                .itemName("치즈 김밥")
                .member(seller)
                .price(4000)
                .stock(10)
                .description("치즈김밥이 참치김밥보다 보통 500원 싸다.")
                .build();
        itemRepository.save(item);

        CartItem cartItem = cartItemRepository.save(
                CartItem.builder()
                        .item(item)
                        .member(buyer)
                        .count(1)
                        .build());

        // when
        cartItemService.deleteCartItem(cartItem.getId());

        // then
        assertEquals(0, cartItemRepository.count());
    }
}