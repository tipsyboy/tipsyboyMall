package study.tipsyboy.tipsyboyMall.order.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import study.tipsyboy.tipsyboyMall.order.dto.OrderCreateDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderInfoResponseDto;
import study.tipsyboy.tipsyboyMall.order.service.OrderService;

@Slf4j
@RequestMapping("/orders")
@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<OrderInfoResponseDto> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        return ResponseEntity.ok(orderService.order(orderCreateDto));
    }

    @GetMapping("/{orderId}") // TODO: ADMIN OR MEMBERS with permissions to view
    public ResponseEntity<OrderInfoResponseDto> getOrderInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        // TODO: Permission Evaluator 구현 && 삭제 권한 추가
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
