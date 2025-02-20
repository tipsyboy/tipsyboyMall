package study.tipsyboy.tipsyboyMall.order.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRole;
import study.tipsyboy.tipsyboyMall.cart.domain.CartItem;
import study.tipsyboy.tipsyboyMall.cart.repository.CartItemRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.order.domain.Delivery;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.OrderItem;
import study.tipsyboy.tipsyboyMall.order.domain.OrderStatus;
import study.tipsyboy.tipsyboyMall.order.dto.*;
import study.tipsyboy.tipsyboyMall.order.exception.OrderException;
import study.tipsyboy.tipsyboyMall.order.exception.OrderExceptionType;
import study.tipsyboy.tipsyboyMall.order.repository.OrderRepository;
import study.tipsyboy.tipsyboyMall.response.PagingResponse;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderService orderService;

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
    @DisplayName("장바구니에 등록된 상품을 주문한다.")
    public void createOrderByCart() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        List<Item> items = IntStream.range(1, 4)
                .mapToObj(i -> Item.builder()
                        .member(member)
                        .itemName("item" + i)
                        .price(i)
                        .stock(i)
                        .description(i + " 번째 상품입니다.")
                        .build())
                .collect(Collectors.toList());
        Item savedItem1 = itemRepository.save(items.get(0));
        Item savedItem2 = itemRepository.save(items.get(1));
        Item savedItem3 = itemRepository.save(items.get(2));

        CartItem cartItem1 = CartItem.builder()
                .item(savedItem1)
                .member(member)
                .count(1)
                .build();
        CartItem cartItem2 = CartItem.builder()
                .item(savedItem2)
                .member(member)
                .count(2)
                .build();
        CartItem cartItem3 = CartItem.builder()
                .item(savedItem3)
                .member(member)
                .count(2)
                .build();
        cartItemRepository.save(cartItem1);
        cartItemRepository.save(cartItem2);
        cartItemRepository.save(cartItem3);

        // when
        List<Long> cartItemIds = List.of(cartItem1.getId(), cartItem3.getId());
        DeliveryRequestDto deliveryRequestDto = new DeliveryRequestDto(
                "간술맨", "01012345678", "1234",
                "1234", "1234", "1234");
        OrderByCartCreateDto orderByCartCreateDto = OrderByCartCreateDto.builder()
                .delivery(deliveryRequestDto)
                .cartItemIds(cartItemIds)
                .build();
        orderService.order(member.getId(), orderByCartCreateDto);

        // then
        Item orderedItem1 = itemRepository.findById(savedItem1.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        Item orderedItem2 = itemRepository.findById(savedItem2.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        Item orderedItem3 = itemRepository.findById(savedItem3.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        assertEquals(savedItem1.getStock() - 1, orderedItem1.getStock()); // 주문 수량만큼 재고 소모1
        assertEquals(savedItem2.getStock(), orderedItem2.getStock()); // 장바구니에 담는 것만으로는 재고가 소모되지 않는다.
        assertEquals(savedItem3.getStock() - 2, orderedItem3.getStock()); // 주문 수량만큼 재고 소모2

        // 주문의 수는 1개
        assertEquals(1, orderRepository.count());

        // 주문 상태
        Order savedOrder = orderRepository.findAll().get(0);
        assertEquals(OrderStatus.ORDER, savedOrder.getOrderStatus());

        // 주문한 장바구니 상품은 삭제된다.
        assertEquals(1L, cartItemRepository.count());
    }


    @Test
    @DisplayName("상품 재고가 부족한 상품을 주문하면 예외가 발생한다.")
    public void orderItemNotEnough() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("파나메라")
                .price(2000)
                .stock(1)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .count(2)
                .member(member)
                .build();
        cartItemRepository.save(cartItem);

        // when
        OrderByCartCreateDto orderByCartCreateDto = OrderByCartCreateDto.builder()
                .cartItemIds(List.of(cartItem.getId()))
                .build();
        // then
        ItemException exception = assertThrows(ItemException.class,
                () -> orderService.order(member.getId(), orderByCartCreateDto));
        assertEquals(ItemExceptionType.ITEM_NOT_ENOUGH, exception.getExceptionType());
    }


    @Test
    @DisplayName("주문을 취소하면 주문의 상태가 변경된다.")
    public void cancelOrderUpdatesStatus() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("카이엔")
                .price(2000)
                .stock(3)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        List<OrderItem> orderItems = List.of(
                OrderItem.builder()
                        .item(item)
                        .orderPrice(item.getPrice())
                        .count(1)
                        .build());

        Delivery delivery = Delivery.builder().build();
        Order order = Order.builder()
                .member(member)
                .orderItems(orderItems)
                .orderStatus(OrderStatus.ORDER)
                .delivery(delivery)
                .build();
        orderRepository.save(order);

        // when
        orderService.cancelOrder(order.getId());

        // then
        Order cancelOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new OrderException(OrderExceptionType.ORDER_NOT_FOUND));
        assertEquals(OrderStatus.CANCEL, cancelOrder.getOrderStatus());
    }

    @Test
    @DisplayName("주문을 취소하면 재고가 복구되어야 한다.")
    public void restoreStockAfterCancel() throws Exception {
        // given - 상품이 있고, 주문을 함
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("콰트로포르테")
                .price(2000)
                .stock(3)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder()
                .item(item)
                .member(member)
                .count(1)
                .build();
        cartItemRepository.save(cartItem);


        DeliveryRequestDto deliveryRequestDto = new DeliveryRequestDto(
                "간술맨", "01012345678", "1234",
                "1234", "1234", "1234");
        OrderByCartCreateDto createDto = OrderByCartCreateDto.builder()
                .cartItemIds(List.of(cartItem.getId()))
                .delivery(deliveryRequestDto)
                .build();

        Long orderId = orderService.order(member.getId(), createDto);

        // when - 주문 취소
        orderService.cancelOrder(orderId);

        // then - 상품의 재고가 복구되어야 한다.
        Item restoredItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        assertEquals(3, restoredItem.getStock());
    }

    @Test
    @DisplayName("주문 id로 주문 내역을 가져온다.")
    public void findOrderById() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .member(member)
                .itemName("벤츠 S 클래스")
                .price(2000)
                .stock(10)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .count(1)
                .build();
        Delivery delivery = Delivery.builder().build();
        Order order = Order.builder()
                .member(member)
                .orderItems(List.of(orderItem))
                .orderStatus(OrderStatus.ORDER)
                .delivery(delivery)
                .build();
        orderRepository.save(order);

        // when
        OrderInfoResponseDto responseDto = orderService.findOrderById(order.getId());

        // then
        assertEquals(responseDto.getId(), order.getId());
        assertEquals(responseDto.getOrderStatus(), order.getOrderStatus());
        assertEquals(
                responseDto.getOrderedDate().truncatedTo(ChronoUnit.SECONDS),
                order.getCreateDate().truncatedTo(ChronoUnit.SECONDS)
        );

        List<OrderItemResponseDto> orderItemDtos = responseDto.getOrderItemList();
        assertEquals(orderItemDtos.size(), order.getOrderItems().size());

        for (int i = 0; i < orderItemDtos.size(); i++) {
            OrderItemResponseDto orderItemDto = orderItemDtos.get(i);
            OrderItem orderitem = order.getOrderItems().get(i);

            assertEquals(orderItemDto.getItemId(), orderItem.getItem().getId());
            assertEquals(orderItemDto.getOrderPrice(), orderItem.getOrderPrice());
            assertEquals(orderItemDto.getCount(), orderItem.getCount());
        }
    }

    @Test
    @DisplayName("해당 id 값의 사용자의 주문 내역을 페이징해서 가져온다.")
    public void getOrderListForPageByMemberId() throws Exception {
        // given
        Member member = Member.builder()
                .email("tipsyboy@gmail.com")
                .password("1234")
                .nickname("간술맨")
                .memberRole(MemberRole.MEMBER)
                .build();
        memberRepository.save(member);

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

        // when
        OrderPagingRequestDto pagingRequestDto = OrderPagingRequestDto.builder()
                .page(2)
                .size(20)
                .build();
        PagingResponse<OrderInfoResponseDto> orderList = orderService.findOrderListByMemberId(pagingRequestDto, member.getId());

        // then
        assertEquals(20, orderList.getSize());
    }
}