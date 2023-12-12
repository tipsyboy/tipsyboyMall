package study.tipsyboy.tipsyboyMall.auth.exception;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.common.exception.BaseException;
import study.tipsyboy.tipsyboyMall.common.exception.ExceptionType;

@Getter
public class AuthException extends BaseException {

    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
