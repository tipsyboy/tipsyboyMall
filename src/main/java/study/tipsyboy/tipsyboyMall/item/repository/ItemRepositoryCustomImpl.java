package study.tipsyboy.tipsyboyMall.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemPagingRequestDto;

import java.util.List;

import static study.tipsyboy.tipsyboyMall.item.domain.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getItems(ItemPagingRequestDto pagingRequestDto) {
        return jpaQueryFactory.selectFrom(item)
                .limit(pagingRequestDto.getSize())
                .offset(pagingRequestDto.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }

    @Override
    public List<Item> getMyItems(ItemPagingRequestDto pagingRequestDto, Member member) {
        return jpaQueryFactory.selectFrom(item)
                .where(
                        item.member.eq(member)
                )
                .limit(pagingRequestDto.getSize())
                .offset(pagingRequestDto.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }
}
