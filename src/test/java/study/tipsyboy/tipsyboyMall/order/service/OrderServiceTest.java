package study.tipsyboy.tipsyboyMall.order.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.domain.ItemRepository;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.OrderItem;
import study.tipsyboy.tipsyboyMall.order.domain.OrderRepository;
import study.tipsyboy.tipsyboyMall.order.domain.OrderStatus;
import study.tipsyboy.tipsyboyMall.order.dto.OrderCreateDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderInfoResponseDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderItemResponseDto;
import study.tipsyboy.tipsyboyMall.order.exception.OrderException;
import study.tipsyboy.tipsyboyMall.order.exception.OrderExceptionType;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    public void after() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 정보를 받아서 주문을 생성, 주문이 생성되고 상품의 재고가 줄어든다.")
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
        Item savedItem1 = itemRepository.save(items.get(0));
        Item savedItem2 = itemRepository.save(items.get(1));
        Item savedItem3 = itemRepository.save(items.get(2));

        // when
        HashMap<Long, Integer> orderInfo = new HashMap<>();
        orderInfo.put(savedItem1.getId(), 1);
        orderInfo.put(savedItem2.getId(), 2);
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .orderInfo(orderInfo)
                .build();
        orderService.order(orderCreateDto);

        // then
        Item orderedItem1 = itemRepository.findById(savedItem1.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        Item orderedItem2 = itemRepository.findById(savedItem2.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        Item orderedItem3 = itemRepository.findById(savedItem3.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        assertEquals(savedItem1.getStock() - 1, orderedItem1.getStock()); // 주문 수량만큼 재고 소모
        assertEquals(savedItem2.getStock() - 2, orderedItem2.getStock());
        assertEquals(savedItem3.getStock(), orderedItem3.getStock()); // 주문 안된 상품은 재고 소모 X

        // 주문의 수는 1개
        assertEquals(1, orderRepository.count());
        // 주문 상태
        Order savedOrder = orderRepository.findAll().get(0);
        assertEquals(OrderStatus.ORDER, savedOrder.getOrderStatus());
    }


    @Test
    @DisplayName("상품 재고가 부족한 상품을 주문하면 예외가 발생한다.")
    public void orderItemNotEnough() throws Exception {
        // given
        Item item = Item.builder()
                .itemName("파나메라")
                .price(2000)
                .stock(1)
                .description("그림의 떡")
                .build();
        itemRepository.save(item);

        // when
        HashMap<Long, Integer> info = new HashMap<>();
        info.put(item.getId(), 2);
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .orderInfo(info)
                .build();
        // then
        ItemException exception = assertThrows(ItemException.class,
                () -> orderService.order(orderCreateDto));
        assertEquals(ItemExceptionType.ITEM_NOT_ENOUGH, exception.getExceptionType());
    }


    @Test
    @DisplayName("주문을 취소하면 주문의 상태가 변경된다.")
    public void cancelOrderUpdatesStatus() throws Exception {
        // given
        Item item = Item.builder()
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

        Order order = Order.builder()
                .orderItems(orderItems)
                .orderStatus(OrderStatus.ORDER)
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
        Item item = Item.builder()
                .itemName("콰트로포르테")
                .price(2000)
                .stock(3)
                .description("그림의 떡")
                .build();
        itemRepository.save(item); // 상품

        HashMap<Long, Integer> info = new HashMap<>(); // 주문서
        info.put(item.getId(), 1);
        OrderInfoResponseDto responseDto = orderService.order(
                OrderCreateDto.builder()
                        .orderInfo(info)
                        .build());// 주문

        // when - 주문 취소
        orderService.cancelOrder(responseDto.getId());

        // then - 상품의 재고가 복구되어야 한다.
        Item restoredItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        assertEquals(3, restoredItem.getStock());
    }

    @Test
    @DisplayName("상품 id로 주문 내역을 가져온다.")
    public void findOrderById() throws Exception {
        // given
        Item item = Item.builder()
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
        Order order = Order.builder()
                .orderItems(List.of(orderItem))
                .orderStatus(OrderStatus.ORDER)
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
}