package study.tipsyboy.tipsyboyMall.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemSearchReqDto;

import java.util.List;

import static study.tipsyboy.tipsyboyMall.item.domain.QItem.item;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Item> getItems(ItemSearchReqDto searchReqDto, Pageable pageable) {
        List<Item> contents = jpaQueryFactory.selectFrom(item)
                .where(
                        titleLike(searchReqDto.getTitle()),
                        sellerLike(searchReqDto.getSeller())
                )
                .limit(searchReqDto.getSize())
                .offset(searchReqDto.getOffset())
                .orderBy(item.id.desc())
                .fetch();

        Long total = jpaQueryFactory.select(item.count())
                .from(item)
                .where(
                        titleLike(searchReqDto.getTitle()),
                        sellerLike(searchReqDto.getSeller())
                )
                .fetchOne();
        return new PageImpl<>(contents, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<Item> getMyItems(ItemSearchReqDto searchReqDto, Member member, Pageable pageable) {
        List<Item> contents = jpaQueryFactory.selectFrom(item)
                .where(
                        item.member.eq(member)
                )
                .limit(searchReqDto.getSize())
                .offset(searchReqDto.getOffset())
                .orderBy(item.id.desc())
                .fetch();
        Long total = jpaQueryFactory.select(item.count())
                .from(item)
                .where(
                        item.member.eq(member)
                )
                .fetchOne();
        return new PageImpl<>(contents, pageable, total == null ? 0 : total);
    }

    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? item.itemName.contains(title) : null;
    }

    private BooleanExpression sellerLike(String seller) {
        return StringUtils.hasText(seller) ? item.member.nickname.contains(seller) : null;
    }
}
