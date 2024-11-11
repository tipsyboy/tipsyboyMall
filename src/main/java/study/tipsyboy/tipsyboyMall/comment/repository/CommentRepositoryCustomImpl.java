package study.tipsyboy.tipsyboyMall.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentPagingReqDto;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

import java.util.List;

import static study.tipsyboy.tipsyboyMall.comment.domain.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Comment> getCommentListByItemId(CommentPagingReqDto commentPagingReqDto, Item item) {

        List<Comment> commentList = jpaQueryFactory.selectFrom(comment)
                .where(
                        comment.item.eq(item),
                        comment.isDeleted.eq(false),
                        comment.parentComment.isNull()
                )
//                .leftJoin(comment.item).fetchJoin()
//                .leftJoin(comment.author).fetchJoin()
//                .leftJoin(comment.parentComment).fetchJoin()
                .offset(commentPagingReqDto.getOffset())
                .limit(commentPagingReqDto.getSize())
                .fetch();

        Long total = jpaQueryFactory.select(comment.count())
                .from(comment)
                .where(
                        comment.item.eq(item)
                )
                .fetchOne();

        return new PageImpl<>(commentList, commentPagingReqDto.getPageable(), total == null ? 0 : total);
    }
}
