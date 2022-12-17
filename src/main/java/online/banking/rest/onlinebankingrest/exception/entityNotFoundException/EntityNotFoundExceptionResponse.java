package online.banking.rest.onlinebankingrest.exception.entityNotFoundException;

import org.springframework.http.HttpStatus;

public class EntityNotFoundExceptionResponse {

    private final String message;
    private final HttpStatus httpStatus;

    public EntityNotFoundExceptionResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
