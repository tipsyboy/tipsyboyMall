package study.tipsyboy.tipsyboyMall.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.order.domain.Order;
import study.tipsyboy.tipsyboyMall.order.domain.QOrder;
import study.tipsyboy.tipsyboyMall.order.dto.OrderPagingRequestDto;

import java.util.List;

import static study.tipsyboy.tipsyboyMall.order.domain.QOrder.*;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements  OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Order> getOrderListByMemberId(OrderPagingRequestDto pagingRequestDto, Member member) {
        return jpaQueryFactory.selectFrom(order)
                .where(
                        order.member.eq(member)
                )
                .offset(pagingRequestDto.getOffset())
                .limit(pagingRequestDto.getSize())
                .orderBy(order.id.desc())
                .fetch();
    }
}
