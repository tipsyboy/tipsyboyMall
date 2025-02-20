package study.tipsyboy.tipsyboyMall.order.api;

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
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.order.domain.Delivery;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.OrderItem;
import study.tipsyboy.tipsyboyMall.order.domain.OrderStatus;
import study.tipsyboy.tipsyboyMall.order.dto.DeliveryRequestDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderByCartCreateDto;
import study.tipsyboy.tipsyboyMall.order.repository.OrderRepository;
import study.tipsyboy.tipsyboyMall.order.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @AfterEach
    public void after() {
        orderRepository.deleteAll();
        cartItemRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("주문 실패 - 등록되지 않은 상품")
    public void orderFailureItemNotFound() throws Exception {
        // expected
        OrderByCartCreateDto orderByCartCreateDto = OrderByCartCreateDto.builder()
                .cartItemIds(List.of(1L, 2L, 3L))
                .build();
        String json = objectMapper.writeValueAsString(orderByCartCreateDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.message").value("해당 상품을 찾을 수 없습니다."))
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("주문 실패 - 재고 부족")
    public void createOrderFailure() throws Exception {
        // given
        Member seller = memberRepository.findAll().get(0);
        Member buyer = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(buyer);

        Item item = Item.builder()
                .member(seller)
                .itemName("아크로리버시티 반포")
                .price(10000)
                .stock(1)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .member(buyer)
                .count(2)
                .build();
        cartItemRepository.save(cartItem);

        // expected
        OrderByCartCreateDto orderByCartCreateDto = OrderByCartCreateDto.builder()
                .cartItemIds(List.of(cartItem.getId()))
                .build();
        String json = objectMapper.writeValueAsString(orderByCartCreateDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.message").value("상품 재고가 부족합니다."))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("주문 성공")
    public void createOrder() throws Exception {
        // given
        Member seller = memberRepository.findAll().get(0);
        Member buyer = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(buyer);

        List<Item> items = IntStream.range(1, 4)
                .mapToObj(i -> Item.builder()
                        .member(seller)
                        .itemName("item" + i)
                        .price(i)
                        .stock(i)
                        .description(i + " 번째 상품입니다.")
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);

        List<CartItem> cartItems = IntStream.range(0, 2)
                .mapToObj(i -> CartItem.builder()
                        .member(buyer)
                        .item(items.get(i))
                        .count(1)
                        .build())
                .collect(Collectors.toList());
        cartItemRepository.saveAll(cartItems);

        DeliveryRequestDto deliveryRequestDto =
                new DeliveryRequestDto("간술맨", "01012345678", "1234", "1234", "1234", "1234");

        // expected
        OrderByCartCreateDto orderByCartCreateDto = OrderByCartCreateDto.builder()
                .cartItemIds(List.of(cartItems.get(0).getId(), cartItems.get(1).getId()))
                .delivery(deliveryRequestDto)
                .build();
        String json = objectMapper.writeValueAsString(orderByCartCreateDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.orderStatus").value("ORDER"))
//                .andExpect(jsonPath("$.orderItemList[0].itemId").value(items.get(0).getId()))
//                .andExpect(jsonPath("$.orderItemList[0].orderPrice").value(1))
//                .andExpect(jsonPath("$.orderItemList[1].itemId").value(items.get(1).getId()))
//                .andExpect(jsonPath("$.orderItemList[1].orderPrice").value(2))
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("주문을 취소한다.")
    public void cancelOrder() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Item item = Item.builder()
                .member(member)
                .itemName("피자 먹고싶다.")
                .price(10000)
                .stock(5)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .count(2)
                .orderPrice(item.getPrice())
                .build();

        Delivery delivery = Delivery.builder()
                .build();

        Order order = Order.builder()
                .orderItems(List.of(orderItem))
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .delivery(delivery)
                .build();
        orderRepository.save(order);

        // expected
        mockMvc.perform(delete("/api/orders/{orderId}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("사용자 id를 통해 주문 목록을 페이징해서 가져온다.")
    public void getOrderListForPageByMemberId() throws Exception {
        // given
        Member member = memberRepository.findAll().get(0);
        Item item = Item.builder()
                .member(member)
                .itemName("벤츠 S 클래스")
                .price(2000)
                .stock(100)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        List<Delivery> deliveries = IntStream.range(0, 50)
                .mapToObj(i -> Delivery.builder().build())
                .toList();
        List<OrderItem> orderItems = IntStream.range(0, 50)
                .mapToObj(i -> OrderItem.builder()
                        .item(item)
                        .orderPrice(item.getPrice())
                        .count(1)
                        .build())
                .toList();
        List<Order> savedOrders = IntStream.range(0, 50)
                .mapToObj(i -> Order.builder()
                        .member(member)
                        .orderItems(List.of(orderItems.get(i)))
                        .orderStatus(OrderStatus.ORDER)
                        .delivery(deliveries.get(i))
                        .build())
                .collect(Collectors.toList());
        orderRepository.saveAll(savedOrders);

        // expected
        mockMvc.perform(get("/api/orders?page=1&size=20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}