package online.banking.rest.onlinebankingrest.exception.transferFaildException;

import org.springframework.http.HttpStatus;

public class TransferFailedExceptionResponse {

    private final String message;
    private final HttpStatus httpStatus;

    public TransferFailedExceptionResponse(String message, HttpStatus httpStatus) {
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
