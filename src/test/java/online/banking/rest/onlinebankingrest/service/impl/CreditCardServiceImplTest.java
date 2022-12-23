package online.banking.rest.onlinebankingrest.service.impl;

import online.banking.rest.onlinebankingrest.entity.CreditCard;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.Role;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;
import online.banking.rest.onlinebankingrest.exception.ExceptionFactory;
import online.banking.rest.onlinebankingrest.exception.entityNotFoundException.EntityNotFoundException;
import online.banking.rest.onlinebankingrest.exception.internalServerError.InternalServerError;
import online.banking.rest.onlinebankingrest.repository.CreditCardRepository;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class CreditCardServiceImplTest {

    @InjectMocks
    private CreditCardServiceImpl creditCardService;

    private CreditCardRepository creditCardRepository = Mockito.mock(CreditCardRepository.class);
    private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private OnlineBankingValidator validator = Mockito.mock(OnlineBankingValidator.class);
    private ExceptionFactory exceptionFactory = Mockito.mock(ExceptionFactory.class);


    @Test
    void getAllCreditCards_success() {
        Mockito.when(creditCardRepository.findAll()).thenReturn(Stream.of(
                new CreditCard(1L, LocalDateTime.of(2014, 9, 19, 14, 5), StatusType.ACTIVE, 111, "1111111111111111", new User()),
                new CreditCard(2L, LocalDateTime.of(2014, 9, 19, 14, 5), StatusType.ACTIVE, 222, "1111111111111112", new User()),
                new CreditCard(3L, LocalDateTime.of(2014, 9, 19, 14, 5), StatusType.ACTIVE, 333, "1111111111111113", new User())
        ).collect(Collectors.toList()));
        assertEquals(3, creditCardService.getAllCreditCards().size());
    }

    @Test
    void findCreditCardById_successCase() {
        Optional<CreditCard> expected = Optional.of(CreditCard.builder()
                .id(5l)
                .expireDate(LocalDateTime.of(2014, 9, 19, 14, 5))
                .cvv(111)
                .statusType(StatusType.ACTIVE)
                .cardNumber("1111111111111111")
                .customer(new User())
                .build());

        Mockito.when(creditCardRepository.findById(expected.get().getId())).thenReturn(expected);
        Optional<CreditCard> actual = creditCardService.findCreditCardById(expected.get().getId());
        assertEquals(expected.get().getId(), actual.get().getId());
    }

    @Test
    void findCreditCardById_getInternalServerError() {
        when(exceptionFactory.invalidId(-5L)).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateId(-5L);

        assertThrows(InternalServerError.class, () -> creditCardService.findCreditCardById(-5L));
    }

    @Test
    void findCreditCardById_getEntityNotFoundException() {
        Optional<CreditCard> creditCard = Optional.empty();
        Long id = 5L;

        when(creditCardRepository.findById(id)).thenReturn(creditCard);
        when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateObject(creditCard, id);

        assertThrows(EntityNotFoundException.class, () -> creditCardService.findCreditCardById(id));
    }

    @Test
    void saveCreditCard_success() {
        CreditCard creditCard = CreditCard.builder()
                .expireDate(LocalDateTime.of(2014, 9, 19, 14, 5))
                .cvv(555)
                .statusType(StatusType.ACTIVE)
                .cardNumber("22222222222222222")
                .build();
        User customer = new User(5L, "name", "surname", "n@email.com", "password", "+79969996969", Role.CUSTOMER);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        creditCardService.saveCreditCard(creditCard, customer.getId());
    }

    @Test
    void saveCreditCard_getInternalServerError() {
        CreditCard creditCard = null;
        Long id = 6L;

        when(exceptionFactory.invalidObjectNull()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateObjectNullCase(creditCard);

        assertThrows(InternalServerError.class, () -> creditCardService.saveCreditCard(creditCard, id));
    }

    @Test
    void saveCreditCard_getEntityNotFoundException() {
        CreditCard creditCard = CreditCard.builder()
                .expireDate(LocalDateTime.of(2014, 9, 19, 14, 5))
                .cvv(555)
                .statusType(StatusType.ACTIVE)
                .cardNumber("22222222222222222")
                .build();
        Optional<User> customer = Optional.empty();
        Long id = 3L;

        when(customerRepository.findById(id)).thenReturn(customer);
        when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateObject(customer, id);

        assertThrows(EntityNotFoundException.class, () -> creditCardService.saveCreditCard(creditCard, id));
    }

    @Test
    void cancelCreditCard_successCase() {
        Long id = 6L;
        Optional<CreditCard> expected = Optional.of(CreditCard.builder()
                .id(6L)
                .expireDate(LocalDateTime.of(2014, 9, 19, 14, 5))
                .cvv(111)
                .statusType(StatusType.ACTIVE)
                .cardNumber("1111111111111111")
                .customer(new User())
                .build());

        when(creditCardRepository.findById(id)).thenReturn(expected);

        creditCardService.cancelCreditCard(id);
    }

    @Test
    void cancelCreditCard_get_InternalServerError() {
        Long id = -6L;

        when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> creditCardService.cancelCreditCard(id));
    }

    @Test
    void cancelCreditCard_get_EntityNotFoundException() {
        Long id = 6L;
        Optional<CreditCard> expected = Optional.empty();

        when(creditCardRepository.findById(id)).thenReturn(expected);
        when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateObject(expected, id);

        assertThrows(EntityNotFoundException.class, () -> creditCardService.cancelCreditCard(id));
    }
}