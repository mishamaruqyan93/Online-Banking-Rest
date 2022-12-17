package online.banking.rest.onlinebankingrest.service;

import online.banking.rest.onlinebankingrest.entity.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Optional<User> findCustomerById(Long id);

    Optional<User> findCustomerByEmail(String email);

    User put(Long id, User customer);

    User save(User customer);

    List<User> getAllCustomers();

    void remove(Long id);
}
