package online.banking.rest.onlinebankingrest.service.impl;

import online.banking.rest.onlinebankingrest.entity.Account;
import online.banking.rest.onlinebankingrest.entity.MoneyTransfer;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.CurrencyType;
import online.banking.rest.onlinebankingrest.entity.enums.Role;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;
import online.banking.rest.onlinebankingrest.exception.ExceptionFactory;
import online.banking.rest.onlinebankingrest.exception.entityNotFoundException.EntityNotFoundException;
import online.banking.rest.onlinebankingrest.exception.internalServerError.InternalServerError;
import online.banking.rest.onlinebankingrest.exception.transferFaildException.TransferFailedException;
import online.banking.rest.onlinebankingrest.repository.AccountRepository;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.repository.MoneyTransferRepository;
import online.banking.rest.onlinebankingrest.validator.OnlineBankingValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    private final MoneyTransferRepository moneyTransferRepository = Mockito.mock(MoneyTransferRepository.class);
    private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private final OnlineBankingValidator validator = Mockito.mock(OnlineBankingValidator.class);
    private final AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
    private final ExceptionFactory exceptionFactory = Mockito.mock(ExceptionFactory.class);

    @Test
    void getAllAccounts() {
        Mockito.when(accountRepository.findAll()).thenReturn(Stream.of(
                new Account(1L, "1111111111111111", StatusType.ACTIVE, CurrencyType.USD, 10.0, new User()),
                new Account(2L, "1111111111111111", StatusType.ACTIVE, CurrencyType.USD, 10.0, new User()),
                new Account(3L, "1111111111111111", StatusType.ACTIVE, CurrencyType.USD, 10.0, new User())
        ).collect(Collectors.toList()));
        assertEquals(3, accountService.getAllAccounts().size());
    }

    @Test
    void findAccountById_successCase() {
        Account expected = new Account(1L, "1111111111111111", StatusType.ACTIVE, CurrencyType.USD, 10.0, new User());

        Mockito.when(accountRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Optional<Account> actual = accountRepository.findById(expected.getId());

        assertEquals(expected.getId(), actual.get().getId());
    }

    @Test
    void findAccountById_get_InternalServerError() {
        Long id = -5L;

        Mockito.when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> accountService.findAccountById(id));
    }

    @Test
    void findAccountById_get_EntityNotFoundException() {
        Optional<Account> expected = Optional.empty();
        Long id = 98L;

        Mockito.when(accountRepository.findById(id)).thenReturn(expected);
        Mockito.when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        Mockito.doThrow(EntityNotFoundException.class).when(validator).validateObject(expected, id);

        assertThrows(EntityNotFoundException.class, () -> accountService.findAccountById(id));
    }


    @Test
    void findAccountByCustomerId_successCase() {
        Long customerId = 6L;
        Account expected = Account.builder()
                .id(5L)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currentBalance(50.0)
                .customer(new User())
                .build();

        Mockito.when(accountRepository.findAccountByCustomerId(customerId)).thenReturn(Optional.of(expected));
        Optional<Account> actual = accountRepository.findAccountByCustomerId(customerId);
        assertEquals(expected, actual.get());
    }

    @Test
    void findAccountByCustomerId_get_InternalServerError() {
        Long id = -5L;

        Mockito.when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> accountService.findAccountByCustomerId(id));
    }

    @Test
    void findAccountByCustomerId_get_EntityNotFoundException() {
        Long id = 89L;
        Optional<Account> expected = Optional.empty();

        Mockito.when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        Mockito.doThrow(EntityNotFoundException.class).when(validator).validateObject(expected, id);

        assertThrows(EntityNotFoundException.class, () -> accountService.findAccountByCustomerId(id));
    }

    @Test
    void save_successCase() {
        Long customerId = 6L;
        User customer = User.builder()
                .id(6L)
                .name("Name")
                .surname("Surname")
                .email("n@email.com")
                .role(Role.CUSTOMER)
                .build();
        Account account = Account.builder()
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currentBalance(50.0)
                .build();
        Account excepted = Account.builder()
                .id(5L)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currentBalance(50.0)
                .customer(customer)
                .build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(accountRepository.save(account)).thenReturn(excepted);

        Account actual = accountService.save(account, customer.getId());
        assertEquals(actual.getCustomer().getEmail(), customer.getEmail());
    }

    @Test
    void save_get_InternalServerError() {
        Long id = -5L;
        Account account = new Account();

        Mockito.when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> accountService.save(account, id));
    }

    @Test
    void save_getInternalServerErrorInNullCase() {
        Account account = null;
        Long customerId = 5L;

        Mockito.when(exceptionFactory.invalidObjectNull()).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateObjectNullCase(account);

        assertThrows(InternalServerError.class, () -> accountService.save(account, customerId));
    }

    @Test
    void save_get_EntityNotFoundException() {
        Long id = 58L;
        Optional<User> customer = Optional.empty();

        Account account = Account.builder()
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currentBalance(50.0)
                .build();

        Mockito.when(customerRepository.findById(id)).thenReturn(customer);
        Mockito.when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        Mockito.doThrow(EntityNotFoundException.class).when(validator).validateObject(customer, id);

        assertThrows(EntityNotFoundException.class, () -> accountService.save(account, id));
    }

    @Test
    void put_successCase() {
        Long id = 5L;
        Account account = Account.builder()
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(50.0)
                .build();
        Long customerId = 6L;
        User customerOnDB = User.builder()
                .id(6L)
                .name("Name")
                .surname("Surname")
                .email("n@email.com")
                .phoneNumber("2222222")
                .role(Role.CUSTOMER)
                .build();
        Account expected = Account.builder()
                .id(id)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(50.0)
                .customer(customerOnDB)
                .build();

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(expected));
        Mockito.when(customerRepository.getReferenceById(customerId)).thenReturn(customerOnDB);
        Mockito.when(accountRepository.save(expected)).thenReturn(expected);

        Account actual = accountService.put(id, account, customerId);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCustomer().getId(), actual.getCustomer().getId());
    }

    @Test
    void remove_successCase() {
        Long id = 5L;
        Account expected = Account.builder()
                .id(id)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(50.0)
                .customer(new User())
                .build();

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(expected));
        accountService.remove(id);
    }

    @Test
    void remove_get_InternalServerError() {
        Long id = -5L;

        Mockito.when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> accountService.remove(id));
    }

    @Test
    void remove_get_EntityNotFoundException() {
        Long id = 55L;
        Optional<Account> account = Optional.empty();

        Mockito.when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        Mockito.doThrow(EntityNotFoundException.class).when(validator).validateObject(account, id);

        assertThrows(EntityNotFoundException.class, () -> accountService.remove(id));
    }

    @Test
    void replenishBalance_success() {
        Long id = 5L;
        Double amount = 20.0;
        Account accountOnDB = Account.builder()
                .id(id)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(50.0)
                .customer(new User())
                .build();
        Account account = Account.builder()
                .id(id)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(70.0)
                .customer(new User())
                .build();

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accountOnDB));
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        assertEquals(account.getCurrentBalance() - amount, accountOnDB.getCurrentBalance());
        accountService.replenishBalance(id, amount);
    }

    @Test
    void replenishBalance_get_InternalServerError() {
        Long id = -5L;
        Double amount = 50.5;

        Mockito.when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> accountService.replenishBalance(id, amount));
    }

    @Test
    void replenishBalance_get_TransferFailedException() {
        Long id = 5L;
        Double amount = -50.5;

        Mockito.when(exceptionFactory.invalidAmount()).thenReturn(new TransferFailedException("Error"));
        Mockito.doThrow(TransferFailedException.class).when(validator).validateAmount(amount);

        assertThrows(TransferFailedException.class, () -> accountService.replenishBalance(id, amount));
    }

    @Test
    void withdrawBalance_successCase() {
        Long id = 5L;
        Double amount = 20.0;
        Account accountOnDB = Account.builder()
                .id(id)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(50.0)
                .customer(new User())
                .build();
        Account account = Account.builder()
                .id(id)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(30.0)
                .customer(new User())
                .build();

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accountOnDB));
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        assertEquals(account.getCurrentBalance() + amount, accountOnDB.getCurrentBalance());
        accountService.replenishBalance(id, amount);
    }

    @Test
    void withdrawBalance_get_InternalServerError() {
        Long id = -5L;
        Double amount = 50.5;

        Mockito.when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> accountService.withdrawBalance(id, amount));
    }

    @Test
    void withdrawBalance_get_EntityNotFoundException() {
        Long id = 58L;
        Double amount = 55.5;
        Optional<Account> account = Optional.empty();

        Mockito.when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        Mockito.doThrow(EntityNotFoundException.class).when(validator).validateObject(account, id);

        assertThrows(EntityNotFoundException.class, () -> accountService.withdrawBalance(id, amount));
    }

    @Test
    void withdrawBalance_TransferFailedException_WhenAmountIsNull() {
        Long id = 5L;
        Double amount = -50.5;

        Mockito.when(exceptionFactory.invalidAmount()).thenReturn(new TransferFailedException("Error"));
        Mockito.doThrow(TransferFailedException.class).when(validator).validateAmount(amount);

        assertThrows(TransferFailedException.class, () -> accountService.withdrawBalance(id, amount));
    }

    @Test
    void withdrawBalance_get_TransferFailedException_WhenBalanceIsEmpty() {
        Long id = 5L;
        Double amount = 55.2;
        Account account = Account.builder()
                .id(5L)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(30.0)
                .customer(new User())
                .build();

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        Mockito.when(exceptionFactory.invalidAccountInBalance()).thenReturn(new TransferFailedException("Error"));
        Mockito.doThrow(TransferFailedException.class).when(validator).validateAccountInBalance(account);

        assertThrows(TransferFailedException.class, () -> accountService.withdrawBalance(id, amount));
    }

    @Test
    void sendToAnotherAccount_successCase() {
        MoneyTransfer moneyTransfer = MoneyTransfer.builder()
                .transferDate(LocalDateTime.of(2022, 10, 10, 10, 25))
                .description("Transfer money")
                .amount(50.5)
                .build();
        Long accountFrom = 1111111111111111L;
        Long accountTo = 2222222222222222L;

        Account accountOnDBFrom = Account.builder()
                .id(5L)
                .accountNumber("1111111111111111")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(30.0)
                .customer(User.builder()
                        .id(6L)
                        .name("Name")
                        .surname("Surname")
                        .email("n@email.com")
                        .phoneNumber("2222222")
                        .role(Role.CUSTOMER)
                        .build())
                .build();
        Account accountOnDBTo = Account.builder()
                .id(10L)
                .accountNumber("1111111111111112")
                .genStatusType(StatusType.ACTIVE)
                .currencyType(CurrencyType.USD)
                .currentBalance(30.0)
                .customer(User.builder()
                        .id(4L)
                        .name("Name2")
                        .surname("Surname2")
                        .email("n2@email.com")
                        .phoneNumber("1111111")
                        .role(Role.CUSTOMER)
                        .build())
                .build();

        Mockito.when(accountRepository.findAccountByAccountNumber(String.valueOf(accountFrom))).thenReturn(Optional.of(accountOnDBFrom));
        Mockito.when(accountRepository.findAccountByAccountNumber(String.valueOf(accountTo))).thenReturn(Optional.of(accountOnDBTo));
        Mockito.when(accountRepository.findById(accountFrom)).thenReturn(Optional.of(accountOnDBFrom));
        Mockito.when(accountRepository.findById(accountTo)).thenReturn(Optional.of(accountOnDBTo));

        accountService.sendToAnotherAccount(moneyTransfer, accountFrom, accountTo);
    }

    @Test
    void sendToAnotherAccount_get_InternalServerError() {
        Long accountFrom = 1111111111111111L;
        Long accountTo = 2222222222222222L;
        MoneyTransfer moneyTransfer = null;

        Mockito.when(exceptionFactory.invalidObjectNull()).thenReturn(new InternalServerError("Error"));
        Mockito.doThrow(InternalServerError.class).when(validator).validateObjectNullCase(moneyTransfer);

        assertThrows(InternalServerError.class, () -> accountService.sendToAnotherAccount(moneyTransfer, accountFrom, accountTo));
    }

    @Test
    void getAllTransfersOfLastYear_success() {
        Mockito.when(moneyTransferRepository.findMoneyTransferByTransferDateOfLastYear()).thenReturn(Stream.of(
                new MoneyTransfer(5L, LocalDateTime.of(2014, 9, 19, 14, 5), "aaa", 50.5, new Account(), new Account()),
                new MoneyTransfer(5L, LocalDateTime.of(2014, 9, 19, 14, 5), "aaa", 50.5, new Account(), new Account()),
                new MoneyTransfer(5L, LocalDateTime.of(2014, 9, 19, 14, 5), "aaa", 50.5, new Account(), new Account()),
                new MoneyTransfer(5L, LocalDateTime.of(2014, 9, 19, 14, 5), "aaa", 50.5, new Account(), new Account())
        ).collect(Collectors.toList()));

        assertEquals(4, moneyTransferRepository.findMoneyTransferByTransferDateOfLastYear().size());
    }
}