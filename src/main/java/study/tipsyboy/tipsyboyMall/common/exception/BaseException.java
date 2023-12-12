package study.tipsyboy.tipsyboyMall.common.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ExceptionType exceptionType;

    public BaseException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }
}
