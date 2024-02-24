package ar.com.api.categories.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiClientErrorException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApiClientErrorException(String message, HttpStatus hStatus) {
        super(message);
        this.httpStatus = hStatus;
    }
}
