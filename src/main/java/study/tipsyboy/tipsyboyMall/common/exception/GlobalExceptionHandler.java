package study.tipsyboy.tipsyboyMall.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.tipsyboy.tipsyboyMall.common.exception.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<ErrorResponse> baseExceptionHandler(BaseException e) {
        log.error("[Base Exception] Type:{}", e.getExceptionType());

        int httpStatusCode = e.getExceptionType().statusCode().value();

        ErrorResponse exceptionBody = ErrorResponse.builder()
                .statusCode(String.valueOf(httpStatusCode))
                .message(e.getExceptionType().message())
                .build();

        return ResponseEntity
                .status(httpStatusCode)
                .body(exceptionBody);
    }

}
