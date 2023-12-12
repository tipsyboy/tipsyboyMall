package study.tipsyboy.tipsyboyMall.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    HttpStatus statusCode();

    String message();

}
