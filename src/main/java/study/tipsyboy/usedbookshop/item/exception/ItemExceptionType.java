package study.tipsyboy.usedbookshop.item.exception;

import org.springframework.http.HttpStatus;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

public enum ItemExceptionType implements ExceptionType {

    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
    ITEM_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다.");

    private final HttpStatus statusCode;
    private final String message;

    ItemExceptionType(HttpStatus statusCode, String message) {
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
