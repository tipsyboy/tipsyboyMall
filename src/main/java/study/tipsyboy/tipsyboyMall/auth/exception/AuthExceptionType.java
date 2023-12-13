package study.tipsyboy.tipsyboyMall.auth.exception;

import org.springframework.http.HttpStatus;
import study.tipsyboy.tipsyboyMall.common.exception.ExceptionType;

public enum AuthExceptionType implements ExceptionType {

    // 404
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // 409
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록되어 있는 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록되어 있는 닉네임입니다.");

    private final HttpStatus statusCode;
    private final String message;

    AuthExceptionType(HttpStatus statusCode, String message) {
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
