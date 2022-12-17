package online.banking.rest.onlinebankingrest.service.impl;

import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.Role;
import online.banking.rest.onlinebankingrest.exception.ExceptionFactory;
import online.banking.rest.onlinebankingrest.exception.duplicateException.DuplicateException;
import online.banking.rest.onlinebankingrest.exception.entityNotFoundException.EntityNotFoundException;
import online.banking.rest.onlinebankingrest.exception.internalServerError.InternalServerError;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.validator.OnlineBankingValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static online.banking.rest.onlinebankingrest.entity.enums.Role.CUSTOMER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);

    private OnlineBankingValidator validator = Mockito.mock(OnlineBankingValidator.class);

    private ExceptionFactory exceptionFactory = Mockito.mock(ExceptionFactory.class);

    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    @Test
    void getAllCustomers_success() {
        when(customerRepository.findAll()).thenReturn(Stream.of(
                new User(1L, "name", "surname", "name@email.com", "password", "25552", CUSTOMER),
                new User(7L, "name", "surname", "surname@email.com", "password", "25552", CUSTOMER)
        ).collect(Collectors.toList()));

        assertEquals(2, customerRepository.findAll().size());
    }

    @Test
    void findCustomerById_success() {
        User excepted = new User(1L, "name", "surname", "email.com", "password", "555", CUSTOMER);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(excepted));

        Optional<User> actual = customerRepository.findById(1L);
        assertEquals(excepted.getEmail(), actual.get().getEmail());
    }

    @Test
    void findCustomerById_getInternalServerError() {
        when(exceptionFactory.invalidId(-1L)).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateId(-1L);

        assertThrows(InternalServerError.class, () -> customerService.findCustomerById(-1L));
    }

    @Test
    void findCustomerById_getEntityNotFoundException() {
        Optional<User> user = Optional.empty();

        when(exceptionFactory.invalidObject(5L)).thenReturn(new EntityNotFoundException("Not Found Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateObject(user, 5L);

        assertThrows(EntityNotFoundException.class, () -> customerService.findCustomerById(5L));
    }

    @Test
    void findCustomerByEmail_success() {
        User excepted = new User(1L, "name", "surname", "email.com", "password", "555", CUSTOMER);
        String email = "email.com";

        when(customerRepository.findByEmail("email.com")).thenReturn(Optional.of(excepted));

        Optional<User> actual = customerRepository.findByEmail(email);
        assertEquals(excepted.getId(), actual.get().getId());
        assertNotNull(excepted.getEmail(), actual.get().getEmail());
    }

    @Test
    void findCustomerByEmail_getInternalServerError() {
        String email = "";

        when(exceptionFactory.invalidEmail()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateEmailAddress(email);

        assertThrows(InternalServerError.class, () -> customerService.findCustomerByEmail(email));
    }

    @Test
    void findCustomerByEmail_getEntityNotFoundException() {
        Optional<User> user = Optional.empty();
        String email = "";

        when(exceptionFactory.invalidCostumerByEmail(email)).thenReturn(new EntityNotFoundException("Not Found Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateCustomerByEmail(user, email);

        assertThrows(EntityNotFoundException.class, () -> customerService.findCustomerByEmail(email));
    }

    @Test
    void save_success() {
        User expected = User.builder()
                .name("name")
                .surname("surname")
                .password("password")
                .email("email.com")
                .role(CUSTOMER)
                .phoneNumber("56565656").build();

        when(customerRepository.save(any())).thenReturn(expected);
        when(passwordEncoder.encode(expected.getPassword())).thenReturn("$2a$10$90mnftmAfxbdDj3pF1GA4u4FtSrXRPdYGAGAvmgeU");

        User actual = customerService.save(expected);
        assertEquals(expected.hashCode(), actual.hashCode());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void save_getInternalServerError_RoleIsWrong() {
        User user = new User(1L, "name", "surname", "email.com", "pass", "555", Role.MANAGER);

        when(exceptionFactory.invalidCustomerRoleType()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateCostumerRoleType(user);

        assertThrows(InternalServerError.class, () -> customerService.save(user));
    }

    @Test
    void save_getInternalServerError_UserIsNull() {
        when(exceptionFactory.invalidObjectNull()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateObjectNullCase(null);
        doCallRealMethod().when(validator).validateCostumerRoleType(null);

        assertThrows(InternalServerError.class, () -> customerService.save(null));
    }

    @Test
    void save_getDuplicateException() {
        User user = new User(1L, "name", "surname", "BankAdmin@mail.com", "pass", "555", CUSTOMER);
        User duplicate = new User(2L, "name2", "surname2", "BankAdmin@mail.com", "pass2", "5552", CUSTOMER);
        Optional<User> result = Optional.of(duplicate);

        doThrow(DuplicateException.class).when(exceptionFactory).duplicateException();
        when(customerRepository.findByEmail("BankAdmin@mail.com")).thenReturn(result);
        doNothing().when(validator).validateCostumerRoleType(user);

        assertThrows(DuplicateException.class, () -> customerService.save(user));
    }

    @Test
    void put_success() {
        Long id = 1L;
        User expected = User.builder()
                .name("nameChanged")
                .surname("surname")
                .email("n@email.com")
                .password("password")
                .phoneNumber("1111")
                .build();
        Optional<User> costumerFromDb = Optional.of(User.builder()
                .id(id)
                .name("name")
                .surname("surname")
                .email("n@email.com")
                .password("password")
                .phoneNumber("1111")
                .build());

        when(customerRepository.findById(id)).thenReturn(costumerFromDb);
        when(passwordEncoder.encode(expected.getPassword())).thenReturn("$2a$10$90mnftmAfxbdDj3pF1GA4u4FtSrXRPdYGAGAvmgeU");
        when(customerRepository.save(expected)).thenReturn(expected);

        User actual = customerService.put(id, expected);
        assertEquals(expected.hashCode(), actual.hashCode());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void put_getInternalServerError_IdIs0() {
        Long id = 0L;
        User costumer = User.builder()
                .name("nameChanged")
                .surname("surname")
                .email("n@email.com")
                .password("password")
                .phoneNumber("1111")
                .build();

        doCallRealMethod().when(validator).validatePutCustomer(id, costumer);
        when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> customerService.put(id, costumer));
    }

    @Test
    void put_getInternalServerError_CostumerIsNull() {
        Long id = 1L;
        doCallRealMethod().when(validator).validatePutCustomer(id, null);

        when(exceptionFactory.invalidObjectNull()).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateObjectNullCase(null);

        assertThrows(InternalServerError.class, () -> customerService.put(id, null));
    }

    @Test
    void put_getEntityNotFoundException() {
        Long id = 1L;
        User costumer = User.builder()
                .name("nameChanged")
                .surname("surname")
                .email("n@email.com")
                .password("password")
                .phoneNumber("1111")
                .build();
        Optional<User> userFromDb = Optional.empty();

        when(customerRepository.findById(id)).thenReturn(userFromDb);
        when(exceptionFactory.invalidObject(1L)).thenReturn(new EntityNotFoundException("Not Found Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateObject(userFromDb, id);

        assertThrows(EntityNotFoundException.class, () -> customerService.put(id, costumer));
    }

    @Test
    void remove_success() {
        Long id = 1L;
        Optional<User> costumerFromDb = Optional.of(User.builder()
                .id(id)
                .name("name")
                .surname("surname")
                .email("n@email.com")
                .password("password")
                .phoneNumber("1111")
                .build());

        when(customerRepository.findById(id)).thenReturn(costumerFromDb);
        customerService.remove(id);
    }

    @Test
    void remove_getInternalServerError_idIs0() {
        Long id = 0L;

        when(exceptionFactory.invalidId(id)).thenReturn(new InternalServerError("Error"));
        doThrow(InternalServerError.class).when(validator).validateId(id);

        assertThrows(InternalServerError.class, () -> customerService.remove(id));
    }

    @Test
    void remove_getEntityNotFoundException() {
        Long id = 1L;
        Optional<User> userFromDb = Optional.empty();

        when(exceptionFactory.invalidObject(id)).thenReturn(new EntityNotFoundException("Not Found Error"));
        doThrow(EntityNotFoundException.class).when(validator).validateObject(userFromDb, id);
        when(customerRepository.findById(id)).thenReturn(userFromDb);

        assertThrows(EntityNotFoundException.class, () -> customerService.remove(id));
    }
}