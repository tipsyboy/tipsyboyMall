package study.tipsyboy.tipsyboyMall.item.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.dto.ItemSearchReqDto;

import java.util.List;

import static study.tipsyboy.tipsyboyMall.item.domain.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Item> getItems(ItemSearchReqDto searchReqDto) {
        return jpaQueryFactory.selectFrom(item)
                .where(
                        titleLike(searchReqDto.getTitle()),
                        sellerLike(searchReqDto.getSeller())
                )
                .limit(searchReqDto.getSize())
                .offset(searchReqDto.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }

    @Override
    public List<Item> getMyItems(ItemSearchReqDto searchReqDto, Member member) {
        return jpaQueryFactory.selectFrom(item)
                .where(
                        item.member.eq(member)
                )
                .limit(searchReqDto.getSize())
                .offset(searchReqDto.getOffset())
                .orderBy(item.id.desc())
                .fetch();
    }

    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? item.itemName.contains(title) : null;
    }

    private BooleanExpression sellerLike(String seller) {
        return StringUtils.hasText(seller) ? item.member.nickname.contains(seller) : null;
    }
}
