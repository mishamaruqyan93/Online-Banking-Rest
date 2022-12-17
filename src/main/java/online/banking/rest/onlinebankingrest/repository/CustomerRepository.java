package online.banking.rest.onlinebankingrest.repository;

import online.banking.rest.onlinebankingrest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
