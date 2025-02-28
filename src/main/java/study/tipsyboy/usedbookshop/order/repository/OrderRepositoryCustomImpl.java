package study.tipsyboy.usedbookshop.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.order.domain.Order;
import study.tipsyboy.usedbookshop.order.domain.OrderStatus;
import study.tipsyboy.usedbookshop.order.dto.OrderPagingRequestDto;

import java.util.List;

import static study.tipsyboy.usedbookshop.order.domain.QOrder.*;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Order> getOrderListByMemberId(OrderPagingRequestDto pagingRequestDto, Member member) {
        List<Order> orderList = jpaQueryFactory.selectFrom(order)
                .where(
                        order.member.eq(member),
                        order.orderStatus.eq(OrderStatus.ORDER)
                )
                .offset(pagingRequestDto.getOffset())
                .limit(pagingRequestDto.getSize())
                .orderBy(order.id.desc())
                .fetch();

        Long total = jpaQueryFactory.select(order.count())
                .from(order)
                .where(
                        order.member.eq(member),
                        order.orderStatus.eq(OrderStatus.ORDER)
                )
                .fetchOne();

        return new PageImpl<>(orderList, pagingRequestDto.getPageable(), total == null ? 0 : total);
    }
}
