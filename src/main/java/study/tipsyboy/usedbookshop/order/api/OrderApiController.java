package study.tipsyboy.usedbookshop.order.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.tipsyboy.usedbookshop.auth.dto.LoginMember;
import study.tipsyboy.usedbookshop.order.dto.OrderByCartCreateDto;
import study.tipsyboy.usedbookshop.order.dto.OrderInfoResponseDto;
import study.tipsyboy.usedbookshop.order.dto.OrderPagingRequestDto;
import study.tipsyboy.usedbookshop.order.service.OrderService;
import study.tipsyboy.usedbookshop.response.PagingResponse;

@Slf4j
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    public ResponseEntity<Long> createOrder(@AuthenticationPrincipal LoginMember loginMember,
                                            @RequestBody OrderByCartCreateDto orderByCartCreateDto) {
        return ResponseEntity.ok(orderService.order(loginMember.getMemberId(), orderByCartCreateDto));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#orderId, 'ORDER', 'READ'))")
    public ResponseEntity<OrderInfoResponseDto> getOrderInfo(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MEMBER')")
    public ResponseEntity<PagingResponse<OrderInfoResponseDto>> getOrderListByMemberId(@ModelAttribute OrderPagingRequestDto requestDto,
                                                                                       @AuthenticationPrincipal LoginMember loginMember) {

        return ResponseEntity.ok(orderService.findOrderListByMemberId(requestDto, loginMember.getMemberId()));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#orderId, 'ORDER', 'DELETE'))")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

}
