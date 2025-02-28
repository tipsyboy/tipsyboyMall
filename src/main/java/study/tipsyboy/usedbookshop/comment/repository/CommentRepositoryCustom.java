package study.tipsyboy.usedbookshop.comment.repository;

import org.springframework.data.domain.Page;
import study.tipsyboy.usedbookshop.comment.domain.Comment;
import study.tipsyboy.usedbookshop.comment.dto.CommentPagingReqDto;
import study.tipsyboy.usedbookshop.item.domain.Item;

public interface CommentRepositoryCustom {

    Page<Comment> getCommentListByItemId(CommentPagingReqDto commentPagingReqDto, Item item);
}
