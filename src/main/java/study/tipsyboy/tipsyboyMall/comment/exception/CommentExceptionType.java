package study.tipsyboy.tipsyboyMall.comment.exception;

import org.springframework.http.HttpStatus;
import study.tipsyboy.tipsyboyMall.common.exception.ExceptionType;

public enum CommentExceptionType implements ExceptionType {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    CANNOT_SELF_LIKE(HttpStatus.BAD_REQUEST, "본인의 댓글은 추천/비추천 할 수 없습니다."),
    ALREADY_LIKED_COMMENT(HttpStatus.CONFLICT, "이미 추천/비추천한 댓글입니다.");

    private final HttpStatus statusCode;
    private final String message;

    CommentExceptionType(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public HttpStatus statusCode() {
        return this.statusCode;
    }

    @Override
    public String message() {
        return this.message;
    }
}
