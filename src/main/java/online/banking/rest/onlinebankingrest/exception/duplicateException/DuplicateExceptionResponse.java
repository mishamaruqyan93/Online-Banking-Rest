package online.banking.rest.onlinebankingrest.exception.duplicateException;

import org.springframework.http.HttpStatus;

public class DuplicateExceptionResponse {

    private final String message;
    private final HttpStatus httpStatus;

    public DuplicateExceptionResponse(String message, HttpStatus httpStatus) {
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
