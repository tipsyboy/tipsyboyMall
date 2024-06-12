package study.tipsyboy.tipsyboyMall.comment.repository;

import org.springframework.data.domain.Page;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentPagingReqDto;
import study.tipsyboy.tipsyboyMall.item.domain.Item;

public interface CommentRepositoryCustom {

    Page<Comment> getCommentListByItemId(CommentPagingReqDto commentPagingReqDto, Item item);
}
