package ar.com.api.categories.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiCustomException extends RuntimeException {

    private final HttpStatus hStatus;

    public ApiCustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.hStatus = httpStatus;
    }

}
