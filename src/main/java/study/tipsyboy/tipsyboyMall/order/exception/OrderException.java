package study.tipsyboy.tipsyboyMall.order.exception;

import lombok.Getter;
import study.tipsyboy.tipsyboyMall.common.exception.BaseException;
import study.tipsyboy.tipsyboyMall.common.exception.ExceptionType;

@Getter
public class OrderException extends BaseException {

    public OrderException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
