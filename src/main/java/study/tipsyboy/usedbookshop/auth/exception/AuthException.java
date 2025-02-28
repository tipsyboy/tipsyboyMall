package study.tipsyboy.usedbookshop.auth.exception;

import lombok.Getter;
import study.tipsyboy.usedbookshop.common.exception.BaseException;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

@Getter
public class AuthException extends BaseException {

    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
