package online.banking.rest.onlinebankingrest.exception.transferAccountPassiveException;

import org.springframework.http.HttpStatus;

public class TransferAccountPassiveExceptionResponse {

    private final String message;
    private final HttpStatus httpStatus;

    public TransferAccountPassiveExceptionResponse(String message, HttpStatus httpStatus) {
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
