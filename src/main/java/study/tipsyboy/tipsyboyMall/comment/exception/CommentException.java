package study.tipsyboy.tipsyboyMall.comment.exception;

import study.tipsyboy.tipsyboyMall.common.exception.BaseException;
import study.tipsyboy.tipsyboyMall.common.exception.ExceptionType;

public class CommentException extends BaseException {

    public CommentException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
