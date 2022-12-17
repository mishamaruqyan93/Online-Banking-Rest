package online.banking.rest.onlinebankingrest.service;

import online.banking.rest.onlinebankingrest.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account put(Long id, Account account, Long customerId);

    Optional<Account> findAccountById(Long id);

    Account findAccountByCustomerId(Long id);

    Account save(Account account, Long customerId);

    List<Account> getAllAccounts();

    void remove(Long id);
}
