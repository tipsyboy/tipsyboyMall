package study.tipsyboy.usedbookshop.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.item.domain.Item;
import study.tipsyboy.usedbookshop.item.dto.ItemSearchReqDto;

import java.util.List;

import static study.tipsyboy.usedbookshop.item.domain.QItem.item;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Item> getItems(ItemSearchReqDto searchReqDto) {
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
        return new PageImpl<>(contents, searchReqDto.getPageable(), total == null ? 0 : total);
    }

    @Override
    public Page<Item> getMyItems(ItemSearchReqDto searchReqDto, Member member) {
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
        return new PageImpl<>(contents, searchReqDto.getPageable(), total == null ? 0 : total);
    }

    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? item.itemName.contains(title) : null;
    }

    private BooleanExpression sellerLike(String seller) {
        return StringUtils.hasText(seller) ? item.member.nickname.contains(seller) : null;
    }
}
