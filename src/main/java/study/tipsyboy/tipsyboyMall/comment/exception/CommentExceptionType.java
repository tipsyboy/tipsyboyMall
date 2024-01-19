package study.tipsyboy.tipsyboyMall.comment.exception;

import org.springframework.http.HttpStatus;
import study.tipsyboy.tipsyboyMall.common.exception.ExceptionType;

public enum CommentExceptionType implements ExceptionType {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.");

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
