package study.tipsyboy.usedbookshop.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.tipsyboy.usedbookshop.auth.domain.Member;
import study.tipsyboy.usedbookshop.auth.domain.MemberRepository;
import study.tipsyboy.usedbookshop.auth.exception.AuthException;
import study.tipsyboy.usedbookshop.auth.exception.AuthExceptionType;
import study.tipsyboy.usedbookshop.comment.domain.Comment;
import study.tipsyboy.usedbookshop.comment.domain.CommentLike;
import study.tipsyboy.usedbookshop.comment.dto.CommentContentEditRequestDto;
import study.tipsyboy.usedbookshop.comment.dto.CommentCreateRequestDto;
import study.tipsyboy.usedbookshop.comment.dto.CommentPagingReqDto;
import study.tipsyboy.usedbookshop.comment.dto.CommentResponseDto;
import study.tipsyboy.usedbookshop.comment.exception.CommentException;
import study.tipsyboy.usedbookshop.comment.exception.CommentExceptionType;
import study.tipsyboy.usedbookshop.comment.repository.CommentLikeRepository;
import study.tipsyboy.usedbookshop.comment.repository.CommentRepository;
import study.tipsyboy.usedbookshop.item.domain.Item;
import study.tipsyboy.usedbookshop.item.exception.ItemException;
import study.tipsyboy.usedbookshop.item.exception.ItemExceptionType;
import study.tipsyboy.usedbookshop.item.repository.ItemRepository;
import study.tipsyboy.usedbookshop.response.PagingResponse;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CommentLikeRepository commentLikeRepository;

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

    @Transactional
    public void likeComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        validCommentLike(comment, member);

        commentLikeRepository.save(new CommentLike(comment, member));
        comment.addLikeCnt();
    }


    @Transactional
    public void dislikeComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(AuthExceptionType.AUTH_NOT_FOUND));

        validCommentLike(comment, member);

        commentLikeRepository.save(new CommentLike(comment, member));
        comment.addDislikeCnt();
    }

    private Comment resolveParentComment(Long parentCommentId) {
        if (parentCommentId == null) {
            return null;
        }
        return commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));
    }

    private void validCommentLike(Comment comment, Member member) {
        if (comment.getAuthor().equals(member)) {
            throw new CommentException(CommentExceptionType.CANNOT_SELF_LIKE);
        }
        if (commentLikeRepository.existsByCommentAndMember(comment, member)) {
            throw new CommentException(CommentExceptionType.ALREADY_LIKED_COMMENT);
        }
    }
}
