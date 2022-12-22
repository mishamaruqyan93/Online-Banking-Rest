package online.banking.rest.onlinebankingrest.service.impl;

import online.banking.rest.onlinebankingrest.entity.Loan;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.Role;
import online.banking.rest.onlinebankingrest.exception.ExceptionFactory;
import online.banking.rest.onlinebankingrest.exception.entityNotFoundException.EntityNotFoundException;
import online.banking.rest.onlinebankingrest.exception.internalServerError.InternalServerError;
import online.banking.rest.onlinebankingrest.exception.transferFaildException.TransferFailedException;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.repository.LoanRepository;
import online.banking.rest.onlinebankingrest.validator.OnlineBankingValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private OnlineBankingValidator validator = Mockito.mock(OnlineBankingValidator.class);
    private ExceptionFactory exceptionFactory = Mockito.mock(ExceptionFactory.class);
    private LoanRepository loanRepository = Mockito.mock(LoanRepository.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

    @Test
    void getAllLoans_success() throws ParseException {
        when(loanRepository.findAll()).thenReturn(Stream.of(
                new Loan(1L, 220.0, 10.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User()),
                new Loan(2L, 220.0, 10.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User()),
                new Loan(3L, 220.0, 10.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User())
        ).collect(Collectors.toList()));
        assertEquals(3, loanService.getAllLoans().size());
    }

    @Test
    void findLoanById_success() throws ParseException {
        Loan excepted = new Loan(1L, 220.0, 10.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User());

        when(loanRepository.findById(excepted.getId())).thenReturn(Optional.of(excepted));
        Loan actual = loanService.findLoanById(excepted.getId());

        assertEquals(excepted.getId(), actual.getId());
    }

    @Test
    void findLoanById_get_InternalServerError() {
        when(exceptionFactory.invalidId(-5L)).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateId(-5L);

        assertThrows(InternalServerError.class, () -> loanService.findLoanById(-5L));
    }

    @Test
    void findLoanById_get_InternalServerError_whenObjectInNull() {
        Optional<Loan> loan = Optional.empty();
        when(exceptionFactory.invalidLoan()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateLoan(loan);
        doCallRealMethod().when(validator).validateLoan(loan);
    }

    @Test
    void payLoanOff_successCase() throws ParseException {
        Loan excepted = new Loan(1L, 220.0, 100.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User());
        Double amount = 50.0;
        Long id = 1L;
        Optional<Loan> loan = Optional.of(excepted);
        when(loanRepository.findById(id)).thenReturn(loan);
        loanService.payLoanOff(id, amount);
    }

    @Test
    void payLoanOff_getInternalServerErrorById() {
        when(exceptionFactory.invalidId(-5L)).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateId(-50L);
        doCallRealMethod().when(validator).validateId(-5L);
    }

    @Test
    void payLoanOff_getTransferFailedException() {
        Double amount = -0.5;
        when(exceptionFactory.invalidAmount()).thenReturn(new TransferFailedException("Error"));
        doThrow(TransferFailedException.class).when(validator).validateAmount(amount);
        doCallRealMethod().when(validator).validateAmount(amount);
    }

    @Test
    void payLoanOff_get_InternalServerErrorByLoan() {
        Optional<Loan> loan = Optional.empty();
        when(exceptionFactory.invalidLoan()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateLoan(loan);
        doCallRealMethod().when(validator).validateLoan(loan);
    }

    @Test
    void payLoanOff_get_internalServerErrorByAmountSize() throws ParseException {
        Loan excepted = new Loan(1L, 220.0, 0.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User());
        when(exceptionFactory.invalidLoanAmountSize()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateLoanAmountSize(excepted);
        doCallRealMethod().when(validator).validateLoanAmountSize(excepted);
    }

    @Test
    void applyLoan_successCase() throws ParseException {
        Loan excepted = new Loan(1L, 220.0, 10.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User());
        User customer = new User(5L, "name", "surname", "n@email.com", "password", "+79969996969", Role.CUSTOMER);

        Optional<User> customer2 = Optional.of(customer);
        when(customerRepository.findById(customer.getId())).thenReturn(customer2);
        loanService.applyLoan(excepted, customer2.get().getId());
    }

    @Test
    void applyLoan_getInternalServerError_byNullCase() {
        Loan loan = null;

        when(exceptionFactory.invalidObjectNull()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateObjectNullCase(loan);
        doCallRealMethod().when(validator).validateObjectNullCase(loan);
    }

    @Test
    void applyLoan_get_EntityNotFoundException() throws ParseException {
        Loan excepted = new Loan(1L, 220.0, 10.0, format.parse("2022-10-10"), format.parse("2022-10-10"), new User());
        Optional<Loan> loan = Optional.of(excepted);
        Long id = 5L;

        when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateObject(loan, id);
        doCallRealMethod().when(validator).validateObject(loan, id);
    }
}