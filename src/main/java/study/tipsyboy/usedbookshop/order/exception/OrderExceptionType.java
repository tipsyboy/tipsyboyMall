package study.tipsyboy.usedbookshop.order.exception;

import org.springframework.http.HttpStatus;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

public enum OrderExceptionType implements ExceptionType {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다.");

    private final HttpStatus statusCode;
    private final String message;

    OrderExceptionType(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public HttpStatus statusCode() {
        return null;
    }

    @Override
    public String message() {
        return null;
    }
}
