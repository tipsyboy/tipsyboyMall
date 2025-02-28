package study.tipsyboy.usedbookshop.config.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import study.tipsyboy.usedbookshop.auth.dto.LoginMember;
import study.tipsyboy.usedbookshop.comment.domain.Comment;
import study.tipsyboy.usedbookshop.comment.exception.CommentException;
import study.tipsyboy.usedbookshop.comment.exception.CommentExceptionType;
import study.tipsyboy.usedbookshop.comment.repository.CommentRepository;

@Slf4j
@RequiredArgsConstructor
public class CommentPermissionHandler implements PermissionHandler {

    private final CommentRepository commentRepository;

    @Override
    public boolean supports(String targetType) {
        return targetType.equals("COMMENT");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Long targetId, String permission) {
        log.info("[Comment - Permission Handler]");

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        Comment comment = commentRepository.findById(targetId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(loginMember.getMemberId())) {
            log.error("[인가 실패] 해당 댓글에 권한이 없습니다.={}", comment.getId());
            return false;
        }

        return true;
    }
}
