package online.banking.rest.onlinebankingrest.service.impl;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.Role;
import online.banking.rest.onlinebankingrest.exception.ExceptionFactory;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.service.CustomerService;
import online.banking.rest.onlinebankingrest.validator.OnlineBankingValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ExceptionFactory onlineBankingExceptionFactory;
    private final CustomerRepository customerRepository;
    private final OnlineBankingValidator validator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<User> findCustomerById(Long id) {
        validator.validateId(id);
        Optional<User> customerById = customerRepository.findById(id);
        validator.validateObject(customerById, id);
        return customerById;
    }

    @Override
    public Optional<User> findCustomerByEmail(String email) {
        validator.validateEmailAddress(email);
        Optional<User> customer = customerRepository.findByEmail(email);
        validator.validateCustomerByEmail(customer, email);
        return customer;
    }

    @Override
    public User save(User customer) {
        validator.validateCostumerRoleType(customer);
        if (customerRepository.findByEmail(customer.getEmail()).isPresent() || customer.getEmail().equals("BankAdmin@mail.com")) {
            onlineBankingExceptionFactory.duplicateException();
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public User put(Long id, User customer) {
        validator.validatePutCustomer(id, customer);
        Optional<User> costumerFromDb = customerRepository.findById(id);
        validator.validateObject(costumerFromDb, id);
        customer.setId(id);
        customer.setRole(Role.CUSTOMER);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public void remove(Long id) {
        validator.validateId(id);
        Optional<User> customer = customerRepository.findById(id);
        validator.validateObject(customer, id);
        customerRepository.deleteById(id);
    }
}
