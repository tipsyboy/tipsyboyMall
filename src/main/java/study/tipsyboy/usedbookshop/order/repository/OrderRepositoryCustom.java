package study.tipsyboy.usedbookshop.order.repository;

import org.springframework.data.domain.Page;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.order.domain.Order;
import study.tipsyboy.usedbookshop.order.dto.OrderPagingRequestDto;

public interface OrderRepositoryCustom {

    Page<Order> getOrderListByMemberId(OrderPagingRequestDto pagingRequestDto, Member member);
}
