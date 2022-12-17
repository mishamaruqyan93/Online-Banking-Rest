package online.banking.rest.onlinebankingrest.validator;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.Account;
import online.banking.rest.onlinebankingrest.entity.Loan;
import online.banking.rest.onlinebankingrest.entity.MoneyTransfer;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.Role;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;
import online.banking.rest.onlinebankingrest.exception.ExceptionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OnlineBankingValidator {

    private final ExceptionFactory exceptionFactory;

    public <T> void validateObject(Optional<T> object, Long id) {
        if (object.isEmpty()) {
            throw exceptionFactory.invalidObject(id);
        }
    }

    public <T> void validateObjectNullCase(T object) {
        if (object == null) {
            throw exceptionFactory.invalidObjectNull();
        }
    }

    public void validateId(Long id) {
        if (id <= 0) {
            throw exceptionFactory.invalidId(id);
        }
    }

    public void validateEmailAddress(String email) {
        if (email == null || email.isEmpty()) {
            throw exceptionFactory.invalidEmail();
        }
    }

    public void validateCustomerByEmail(Optional<User> customer, String email) {
        if (customer.isEmpty()) {
            throw exceptionFactory.invalidCostumerByEmail(email);
        }
    }

    public void validateAccountActiveAndBalance(MoneyTransfer moneyTransfer) {
        validateAccountInBalance(moneyTransfer.getAccountFrom());
        if (moneyTransfer.getAccountTo().getGenStatusType() == StatusType.PASSIVE ||
                moneyTransfer.getAccountFrom().getGenStatusType() == StatusType.PASSIVE) {
            throw exceptionFactory.invalidAccountActiveAndBalance();
        }
    }

    public void validateAccountInBalance(Account account) {
        if (account.getCurrentBalance() <= 0) {
            throw exceptionFactory.invalidAccountInBalance();
        }
    }

    public void validateAmount(Double amount) {
        if (amount <= 0) {
            throw exceptionFactory.invalidAmount();
        }
    }

    public void validateCostumerRoleType(User customer) {
        validateObjectNullCase(customer);
        if (customer.getRole() == Role.MANAGER) {
            throw exceptionFactory.invalidCustomerRoleType();
        }
    }

    public void validateLoanAmountSize(Loan loan) {
        if (loan.getPaymentAmount() <= 0) {
            throw exceptionFactory.invalidLoanAmountSize();
        }
    }

    public void validateLoan(Optional<Loan> loan) {
        if (loan.isEmpty()) {
            throw exceptionFactory.invalidLoan();
        }
    }

    public void validateLoanIdAndAmount(Long id, Double amount) {
        validateId(id);
        validateAmount(amount);
    }

    public void validatePutAccount(Long id, Account account) {
        validateId(id);
        validateObjectNullCase(account);
    }

    public void validatePutCustomer(Long id, User customer) {
        validateId(id);
        validateObjectNullCase(customer);
    }

    public void validateAccount(Optional<Account> account) {
        if (account.isEmpty()) {
            throw exceptionFactory.AccountNotFoundException();
        }
    }
}