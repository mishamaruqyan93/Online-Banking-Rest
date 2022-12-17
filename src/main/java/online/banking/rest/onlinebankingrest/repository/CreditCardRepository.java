package online.banking.rest.onlinebankingrest.repository;

import online.banking.rest.onlinebankingrest.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
