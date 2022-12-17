package online.banking.rest.onlinebankingrest.exception.internalServerError;

import org.springframework.http.HttpStatus;

public class ServerErrorResponse {

    private final String message;
    private final HttpStatus httpStatus;

    public ServerErrorResponse(String message, HttpStatus httpStatus) {
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
