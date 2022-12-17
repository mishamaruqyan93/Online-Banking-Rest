package online.banking.rest.onlinebankingrest.service;

import online.banking.rest.onlinebankingrest.entity.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    List<CreditCard> getAllCreditCards();

    Optional<CreditCard> findCreditCardById(Long id);

    void cancelCreditCard(Long id);

    void saveCreditCard(CreditCard creditCard, Long customerId);
}
