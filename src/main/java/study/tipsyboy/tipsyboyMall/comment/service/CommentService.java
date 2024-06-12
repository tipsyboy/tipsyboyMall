package study.tipsyboy.tipsyboyMall.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.tipsyboyMall.auth.domain.Member;
import study.tipsyboy.tipsyboyMall.auth.domain.MemberRepository;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthException;
import study.tipsyboy.tipsyboyMall.auth.exception.AuthExceptionType;
import study.tipsyboy.tipsyboyMall.comment.domain.Comment;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentContentEditRequestDto;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentCreateRequestDto;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentPagingReqDto;
import study.tipsyboy.tipsyboyMall.comment.dto.CommentResponseDto;
import study.tipsyboy.tipsyboyMall.comment.exception.CommentException;
import study.tipsyboy.tipsyboyMall.comment.exception.CommentExceptionType;
import study.tipsyboy.tipsyboyMall.comment.repository.CommentRepository;
import study.tipsyboy.tipsyboyMall.item.domain.Item;
import study.tipsyboy.tipsyboyMall.item.exception.ItemException;
import study.tipsyboy.tipsyboyMall.item.exception.ItemExceptionType;
import study.tipsyboy.tipsyboyMall.item.repository.ItemRepository;
import study.tipsyboy.tipsyboyMall.response.PagingResponse;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(Long memberId, CommentCreateRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));
        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));
        Comment parentComment = resolveParentComment(requestDto.getParentCommentId());

        Comment comment = Comment.builder()
                .author(member)
                .item(item)
                .content(requestDto.getContent())
                .parentComment(parentComment)
                .build();

        return commentRepository.save(comment).getId();
    }

    public PagingResponse<CommentResponseDto> readCommentByItem(CommentPagingReqDto commentPagingReqDto, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemExceptionType.ITEM_NOT_FOUND));

        Page<Comment> commentList = commentRepository.getCommentListByItemId(commentPagingReqDto, item);

        return new PagingResponse<>(commentList, CommentResponseDto.class);
    }

    @Transactional
    public Long editCommentContent(Long commentId, CommentContentEditRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        comment.editContent(requestDto.getContent());

        return comment.getId();
    }

    @Transactional
    public Long deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        comment.delete();

        return comment.getId();
    }


    private Comment resolveParentComment(Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }

        return commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));
    }
}
