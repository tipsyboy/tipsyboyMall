package study.tipsyboy.usedbookshop.order.exception;

import lombok.Getter;
import study.tipsyboy.usedbookshop.common.exception.BaseException;
import study.tipsyboy.usedbookshop.common.exception.ExceptionType;

@Getter
public class OrderException extends BaseException {

    public OrderException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
