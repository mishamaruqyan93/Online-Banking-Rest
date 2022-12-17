package online.banking.rest.onlinebankingrest.exception;

import lombok.extern.slf4j.Slf4j;
import online.banking.rest.onlinebankingrest.exception.duplicateException.DuplicateException;
import online.banking.rest.onlinebankingrest.exception.entityNotFoundException.EntityNotFoundException;
import online.banking.rest.onlinebankingrest.exception.internalServerError.InternalServerError;
import online.banking.rest.onlinebankingrest.exception.transferAccountPassiveException.TransferAccountPassiveException;
import online.banking.rest.onlinebankingrest.exception.transferFaildException.TransferFailedException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class ExceptionFactory {

    public EntityNotFoundException invalidObject(Long id) {
        log.error("Object does not exist by this id: {} {}", id, new Date());
        return new EntityNotFoundException("Object does not exist by this id: " + id);
    }

    public InternalServerError invalidId(Long id) {
        log.error("The id can not be 0 or less than 0`  id: {} {}", id, new Date());
        return new InternalServerError("The id can not be 0 or less than 0 : " + id);
    }

    public InternalServerError invalidObjectNull() {
        log.error("Object was passed null {}", new Date());
        return new InternalServerError("Object was passed null");
    }

    public void duplicateException() {
        throw new DuplicateException("This email is already in use");
    }

    public InternalServerError invalidEmail() {
        log.error("Email  was passed null {}", new Date());
        return new InternalServerError("Email passed was null");
    }

    public EntityNotFoundException invalidCostumerByEmail(String email) {
        log.error("Customer  was passed null by this email {}", new Date());
        throw new EntityNotFoundException("Customer does not exist by this email: " + email);
    }

    public TransferAccountPassiveException invalidAccountActiveAndBalance() {
        log.error("This account is not active! {}", new Date());
        throw new TransferAccountPassiveException("Account is not active!");
    }

    public TransferFailedException invalidAccountInBalance() {
        log.error("There are not enough funds on this account : {}", new Date());
        throw new TransferFailedException("You haven't enough money!");
    }

    public TransferFailedException invalidAmount() {
        log.error("The balance of this transfer was 0 or below 0 {}", new Date());
        throw new TransferFailedException("Balance cannot be 0 or below 0");
    }

    public InternalServerError invalidCustomerRoleType() {
        log.error("Role should only be customer");
        throw new InternalServerError("Only the head of the bank can add a manager");
    }

    public InternalServerError invalidLoanAmountSize() {
        log.info("Loan has already paid");
        throw new InternalServerError("Loan has already paid: Thanks for using our services");
    }

    public InternalServerError invalidLoan() {
        log.error("Object of type loan does not exist");
        throw new InternalServerError("No value present.");
    }

    public EntityNotFoundException AccountNotFoundException() {
        log.error("Object does not exist by this account-number");
        throw new EntityNotFoundException("Object does not exist by this account-number");
    }
}
