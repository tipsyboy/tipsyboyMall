package study.tipsyboy.usedbookshop.common.exception.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final String statusCode;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String statusCode, String message, Map<String, String> validation) {
        this.statusCode = statusCode;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

}
