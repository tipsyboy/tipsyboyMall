package study.tipsyboy.usedbookshop.comment.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.tipsyboy.usedbookshop.auth.dto.LoginMember;
import study.tipsyboy.usedbookshop.comment.dto.CommentContentEditRequestDto;
import study.tipsyboy.usedbookshop.comment.dto.CommentCreateRequestDto;
import study.tipsyboy.usedbookshop.comment.dto.CommentPagingReqDto;
import study.tipsyboy.usedbookshop.comment.dto.CommentResponseDto;
import study.tipsyboy.usedbookshop.comment.service.CommentService;
import study.tipsyboy.usedbookshop.response.PagingResponse;

@Slf4j
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')")
    public ResponseEntity<Long> createComment(@AuthenticationPrincipal LoginMember loginMember,
                                              @RequestBody CommentCreateRequestDto requestDto) {
        return ResponseEntity.ok(commentService.create(loginMember.getMemberId(), requestDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<PagingResponse<CommentResponseDto>> readComment(@PathVariable Long itemId,
                                                                          @ModelAttribute CommentPagingReqDto commentPagingReqDto) {
        return ResponseEntity.ok(commentService.readCommentByItem(commentPagingReqDto, itemId));
    }

    @PatchMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#commentId, 'COMMENT', 'PATCH'))")
    public ResponseEntity<Long> editComment(@PathVariable Long commentId,
                                            @RequestBody CommentContentEditRequestDto requestDto) {
        return ResponseEntity.ok(commentService.editCommentContent(commentId, requestDto));
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || (hasRole('ROLE_MEMBER') && hasPermission(#commentId, 'COMMENT', 'DELETE'))")
    public ResponseEntity<Long> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId,
                                            @AuthenticationPrincipal LoginMember loginMember) {
        commentService.likeComment(commentId, loginMember.getMemberId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{commentId}/dislike")
    public ResponseEntity<Void> dislikeComment(@PathVariable Long commentId,
                                               @AuthenticationPrincipal LoginMember loginMember) {
        commentService.dislikeComment(commentId, loginMember.getMemberId());
        return ResponseEntity.ok().build();
    }
}
