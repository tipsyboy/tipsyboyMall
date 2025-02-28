package study.tipsyboy.usedbookshop.likeitem.exception;

import study.tipsyboy.usedbookshop.common.exception.BaseException;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

public class LikeItemException extends BaseException {

    public LikeItemException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
