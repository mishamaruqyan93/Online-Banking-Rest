package online.banking.rest.onlinebankingrest.service.impl;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.CreditCard;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;
import online.banking.rest.onlinebankingrest.repository.CreditCardRepository;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.service.CreditCardService;
import online.banking.rest.onlinebankingrest.validator.OnlineBankingValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CustomerRepository customerRepository;
    private final OnlineBankingValidator validator;

    @Override
    public List<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    @Override
    public Optional<CreditCard> findCreditCardById(Long id) {
        validator.validateId(id);
        Optional<CreditCard> creditCardFromDb = creditCardRepository.findById(id);
        validator.validateObject(creditCardFromDb, id);
        return creditCardFromDb;
    }

    @Override
    public void saveCreditCard(CreditCard creditCard, Long customerId) {
        validator.validateObjectNullCase(creditCard);
        Optional<User> customer = customerRepository.findById(customerId);
        validator.validateObject(customer, customerId);
        creditCard.setCustomer(customerRepository.getReferenceById(customerId));
        creditCardRepository.save(creditCard);
    }

    @Override
    public void cancelCreditCard(Long id) {
        validator.validateId(id);
        Optional<CreditCard> creditCardByDB = creditCardRepository.findById(id);
        validator.validateObject(creditCardByDB, id);
        creditCardByDB.get().setStatusType(StatusType.PASSIVE);
        creditCardByDB.get().setExpireDate(LocalDateTime.now());
        creditCardRepository.save(creditCardByDB.get());
    }
}
