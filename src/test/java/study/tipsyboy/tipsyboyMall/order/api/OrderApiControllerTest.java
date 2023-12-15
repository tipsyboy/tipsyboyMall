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
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.domain.ItemRepository;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.OrderItem;
import study.tipsyboy.tipsyboyMall.order.domain.OrderRepository;
import study.tipsyboy.tipsyboyMall.order.domain.OrderStatus;
import study.tipsyboy.tipsyboyMall.order.dto.OrderCreateDto;
import study.tipsyboy.tipsyboyMall.order.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @AfterEach
    public void after() {
        memberRepository.deleteAll();
        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("주문 실패 - 등록되지 않은 상품")
    public void orderFailureItemNotFound() throws Exception {
        // expected
        HashMap<Long, Integer> orderInfoMap = new HashMap<>();
        orderInfoMap.put(1L, 1);
        orderInfoMap.put(2L, 1);
        orderInfoMap.put(3L, 1);

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .orderInfo(orderInfoMap)
                .build();
        String json = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value("404"))
                .andExpect(jsonPath("$.message").value("해당 상품을 찾을 수 없습니다."))
                .andDo(print());
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("주문 실패 - 재고 부족")
    public void createOrderFailure() throws Exception {
        // given
        Item item = Item.builder()
                .itemName("아크로리버시티 반포")
                .price(10000)
                .stock(1)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        // expected
        HashMap<Long, Integer> orderInfoMap = new HashMap<>();
        orderInfoMap.put(item.getId(), 2);

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .orderInfo(orderInfoMap)
                .build();
        String json = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.statusCode").value("400"))
                .andExpect(jsonPath("$.message").value("상품 재고가 부족합니다."))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("주문 실패 - 잔액 부족")
    public void orderFailureInsufficientBalance() throws Exception {
        // TODO: 주문 실패 - 잔액 부족
        // given
        // when
        // then
    }

    @Test
    @CustomWithMockUser(memberRole = MemberRole.MEMBER)
    @DisplayName("주문 성공")
    public void createOrder() throws Exception {
        // given
        List<Item> items = IntStream.range(1, 4)
                .mapToObj(i -> Item.builder()
                        .itemName("item" + i)
                        .price(i)
                        .stock(i)
                        .description(i + " 번째 상품입니다.")
                        .build())
                .collect(Collectors.toList());
        itemRepository.saveAll(items);

        // expected
        HashMap<Long, Integer> orderInfoMap = new HashMap<>();
        Long itemId1 = itemRepository.findAll().get(0).getId();
        Long itemId2 = itemRepository.findAll().get(1).getId();
        Long itemId3 = itemRepository.findAll().get(2).getId();
        orderInfoMap.put(itemId1, 1);
        orderInfoMap.put(itemId2, 2);
        orderInfoMap.put(itemId3, 3);

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .orderInfo(orderInfoMap)
                .build();
        String json = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("ORDER"))
                .andExpect(jsonPath("$.orderItemList[0].itemId").value(itemId1))
                .andExpect(jsonPath("$.orderItemList[0].orderPrice").value(1))
                .andExpect(jsonPath("$.orderItemList[1].itemId").value(itemId2))
                .andExpect(jsonPath("$.orderItemList[1].orderPrice").value(2))
                .andExpect(jsonPath("$.orderItemList[2].itemId").value(itemId3))
                .andExpect(jsonPath("$.orderItemList[2].orderPrice").value(3))
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

        Order order = Order.builder()
                .orderItems(List.of(orderItem))
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .build();
        orderRepository.save(order);

        // expected
        mockMvc.perform(delete("/orders/{orderId}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}