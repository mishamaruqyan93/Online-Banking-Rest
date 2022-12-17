package online.banking.rest.onlinebankingrest.exception;

import online.banking.rest.onlinebankingrest.exception.duplicateException.DuplicateException;
import online.banking.rest.onlinebankingrest.exception.duplicateException.DuplicateExceptionResponse;
import online.banking.rest.onlinebankingrest.exception.entityNotFoundException.EntityNotFoundException;
import online.banking.rest.onlinebankingrest.exception.entityNotFoundException.EntityNotFoundExceptionResponse;
import online.banking.rest.onlinebankingrest.exception.internalServerError.InternalServerError;
import online.banking.rest.onlinebankingrest.exception.internalServerError.ServerErrorResponse;
import online.banking.rest.onlinebankingrest.exception.transferAccountPassiveException.TransferAccountPassiveException;
import online.banking.rest.onlinebankingrest.exception.transferAccountPassiveException.TransferAccountPassiveExceptionResponse;
import online.banking.rest.onlinebankingrest.exception.transferFaildException.TransferFailedException;
import online.banking.rest.onlinebankingrest.exception.transferFaildException.TransferFailedExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DuplicateException.class)
    public ResponseEntity<Object> handlerDuplicateException(DuplicateException e) {
        HttpStatus conflict = HttpStatus.CONFLICT;
        DuplicateExceptionResponse duplicateExceptionBody = new DuplicateExceptionResponse(
                e.getMessage(),
                conflict
        );
        return new ResponseEntity<>(duplicateExceptionBody, conflict);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handlerEntityNotFoundException(EntityNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        EntityNotFoundExceptionResponse entityNotFoundExceptionBody = new EntityNotFoundExceptionResponse(
                e.getMessage(),
                notFound
        );
        return new ResponseEntity<>(entityNotFoundExceptionBody, notFound);
    }

    @ExceptionHandler(value = TransferAccountPassiveException.class)
    public ResponseEntity<Object> handlerEntityNotFoundException(TransferAccountPassiveException e) {
        HttpStatus forbidden = HttpStatus.FORBIDDEN;
        TransferAccountPassiveExceptionResponse transferAccountPassiveExceptionBody = new TransferAccountPassiveExceptionResponse(
                e.getMessage(),
                forbidden
        );
        return new ResponseEntity<>(transferAccountPassiveExceptionBody, forbidden);
    }

    @ExceptionHandler(value = TransferFailedException.class)
    public ResponseEntity<Object> handlerEntityNotFoundException(TransferFailedException e) {
        HttpStatus paymentRequired = HttpStatus.PAYMENT_REQUIRED;
        TransferFailedExceptionResponse transferAccountPassiveExceptionBody = new TransferFailedExceptionResponse(
                e.getMessage(),
                paymentRequired
        );
        return new ResponseEntity<>(transferAccountPassiveExceptionBody, paymentRequired);
    }

    @ExceptionHandler(value = InternalServerError.class)
    public ResponseEntity<Object> handlerEntityNotFoundException(InternalServerError e) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ServerErrorResponse serverErrorBody = new ServerErrorResponse(
                e.getMessage(),
                internalServerError
        );
        return new ResponseEntity<>(serverErrorBody, internalServerError);
    }
}