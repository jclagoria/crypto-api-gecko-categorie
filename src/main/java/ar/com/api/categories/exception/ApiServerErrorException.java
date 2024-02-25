package ar.com.api.categories.exception;

import ar.com.api.categories.enums.ErrorTypeEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiServerErrorException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String originalMessage;
    private final ErrorTypeEnum errorType;

    public ApiServerErrorException(String message, String originalMessage,
                                   HttpStatus hStatus, ErrorTypeEnum errorType) {
        super(message);
        this.httpStatus = hStatus;
        this.originalMessage = originalMessage;
        this.errorType = errorType;
    }
}
