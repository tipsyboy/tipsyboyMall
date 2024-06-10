package study.tipsyboy.tipsyboyMall.order.repository;

import org.springframework.data.domain.Page;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.dto.OrderPagingRequestDto;

import java.util.List;

public interface OrderRepositoryCustom {

    Page<Order> getOrderListByMemberId(OrderPagingRequestDto pagingRequestDto, Member member);
}
