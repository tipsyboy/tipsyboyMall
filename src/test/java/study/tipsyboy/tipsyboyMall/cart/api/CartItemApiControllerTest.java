package study.tipsyboy.tipsyboyMall.cart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.annotation.CustomWithMockUser;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemSaveRequestDto;
import study.tipsyboy.tipsyboyMall.cart.dto.CartItemUpdateRequestDto;
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CartItemApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void after() {
        cartItemRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("상품을 장바구니에 넣는다.")
    public void addToCart() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0); // 구매자 간술맨
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .member(seller)
                .itemName("오브젝트")
                .price(38000)
                .stock(10)
                .description("코드로 이해하는 객체지향 설계")
                .build();
        itemRepository.save(item);

        CartItemSaveRequestDto requestDto = CartItemSaveRequestDto.builder()
                .itemId(item.getId())
                .count(2)
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/cartItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("장바구니 상품을 조회한다.")
    public void getCartItems() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0); // 구매자 간술맨
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .member(seller)
                .itemName("오브젝트")
                .price(38000)
                .stock(10)
                .description("코드로 이해하는 객체지향 설계")
                .build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .member(buyer)
                .count(2)
                .build();
        cartItemRepository.save(cartItem);

        // expected
        mockMvc.perform(get("/api/cartItem")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cartItemId").value(cartItem.getId()))
                .andDo(print());
    }


    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("장바구니 상품의 수량을 변경한다.")
    public void updateCartItemCount() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0); // 구매자 간술맨
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .member(seller)
                .itemName("페퍼로니 피자")
                .price(25900)
                .stock(10)
                .description("페퍼로니 피자가 근본이다. 비싸니까 하나만 먹어야지")
                .build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .member(buyer)
                .count(2)
                .build();
        cartItemRepository.save(cartItem);

        CartItemUpdateRequestDto requestDto = CartItemUpdateRequestDto.builder()
                .itemId(item.getId())
                .count(1)
                .build();
        String json = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(patch("/api/cartItem/{cartItemId}", cartItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        CartItem findCartItem = cartItemRepository.findAll().get(0);
        assertEquals(1, findCartItem.getCount());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("장바구니 품목을 삭제한다.")
    public void deleteCartItem() throws Exception {
        // given
        Member buyer = memberRepository.findAll().get(0); // 구매자 간술맨
        Member seller = Member.builder()
                .email("tipsyboy1@gmail.com")
                .nickname("혼술맨")
                .password("1234")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(seller);

        Item item = Item.builder()
                .member(seller)
                .itemName("페퍼로니 피자")
                .price(25900)
                .stock(10)
                .description("페퍼로니 피자가 근본이다. 비싸니까 하나만 먹어야지")
                .build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .member(buyer)
                .count(2)
                .build();
        cartItemRepository.save(cartItem);

        // expected
        mockMvc.perform(delete("/api/cartItem/{cartItemId}", cartItem.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(0, cartItemRepository.count());
    }
}