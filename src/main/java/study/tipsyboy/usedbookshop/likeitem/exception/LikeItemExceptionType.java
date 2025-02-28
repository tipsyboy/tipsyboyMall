package study.tipsyboy.usedbookshop.likeitem.exception;

import org.springframework.http.HttpStatus;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

public enum LikeItemExceptionType implements ExceptionType {

    NOT_FOUND_LIKE_ITEM(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다."),

    DUPLICATE_LIKE_ITEM(HttpStatus.CONFLICT, "이미 관심상품으로 등록한 상품입니다.");

    private final HttpStatus statusCode;
    private final String message;

    LikeItemExceptionType(HttpStatus statusCode, String message) {
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
