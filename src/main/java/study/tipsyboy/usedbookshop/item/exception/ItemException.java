package study.tipsyboy.usedbookshop.item.exception;

import lombok.Getter;
import study.tipsyboy.usedbookshop.common.exception.BaseException;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

@Getter
public class ItemException extends BaseException {

    public ItemException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
