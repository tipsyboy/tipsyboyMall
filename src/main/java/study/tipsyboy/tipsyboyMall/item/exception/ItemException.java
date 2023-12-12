package study.tipsyboy.tipsyboyMall.item.exception;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.common.exception.BaseException;
import study.tipsyboy.tipsyboyMall.common.exception.ExceptionType;

@Getter
public class ItemException extends BaseException {

    public ItemException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
