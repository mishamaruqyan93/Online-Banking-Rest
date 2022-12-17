package online.banking.rest.onlinebankingrest.repository;

import online.banking.rest.onlinebankingrest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByCustomerId(Long id);

    Optional<Account> findAccountByAccountNumber(String accountNumber);
}
