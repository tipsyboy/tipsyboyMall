package study.tipsyboy.tipsyboyMall.order.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.tipsyboy.tipsyboyMall.auth.dto.LoginMember;
import study.tipsyboy.tipsyboyMall.order.dto.OrderByCartCreateDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderInfoResponseDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderPagingRequestDto;
import study.tipsyboy.tipsyboyMall.order.dto.OrderPreviewItemResponseDto;
import study.tipsyboy.tipsyboyMall.order.service.OrderService;

import java.util.List;

@Slf4j
@RequestMapping("/orders")
@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderInfoResponseDto> createOrder(@AuthenticationPrincipal LoginMember loginMember,
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
    public ResponseEntity<List<OrderInfoResponseDto>> getOrderListByMemberId(
            @ModelAttribute OrderPagingRequestDto requestDto,
            @AuthenticationPrincipal LoginMember loginMember) {

        return ResponseEntity.ok(orderService.findOrderListByMemberId(requestDto, loginMember.getMemberId()));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#orderId, 'ORDER', 'DELETE'))")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    // order preview
    @PostMapping("/preview")
    public ResponseEntity<List<OrderPreviewItemResponseDto>> viewOrderPreview(@RequestBody List<Long> selectedItems) {
        return ResponseEntity.ok(orderService.preview(selectedItems));
    }
}
