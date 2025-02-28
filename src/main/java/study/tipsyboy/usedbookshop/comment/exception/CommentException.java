package study.tipsyboy.usedbookshop.comment.exception;

import study.tipsyboy.usedbookshop.common.exception.BaseException;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

public class CommentException extends BaseException {

    public CommentException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
