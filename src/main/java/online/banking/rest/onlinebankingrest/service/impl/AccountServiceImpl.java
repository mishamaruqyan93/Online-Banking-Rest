package online.banking.rest.onlinebankingrest.service.impl;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.Account;
import online.banking.rest.onlinebankingrest.entity.MoneyTransfer;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.entity.enums.CurrencyType;
import online.banking.rest.onlinebankingrest.repository.AccountRepository;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.repository.MoneyTransferRepository;
import online.banking.rest.onlinebankingrest.service.AccountService;
import online.banking.rest.onlinebankingrest.service.MoneyTransferService;
import online.banking.rest.onlinebankingrest.validator.OnlineBankingValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;
    private final CurrencyServiceIMpl currencyServiceIMpl;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final OnlineBankingValidator validator;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        validator.validateId(id);
        Optional<Account> accountById = accountRepository.findById(id);
        validator.validateObject(accountById, id);
        return accountById;
    }

    @Override
    public Account findAccountByCustomerId(Long id) {
        validator.validateId(id);
        Optional<Account> accountFromDb = accountRepository.findAccountByCustomerId(id);
        validator.validateObject(accountFromDb, id);
        return accountFromDb.get();
    }

    @Override
    public Account save(Account account, Long customerId) {
        account.setCustomer(customerRepository.getReferenceById(customerId));
        Optional<User> customer = customerRepository.findById(customerId);
        validator.validateObject(customer, customerId);
        validator.validateObjectNullCase(account);
        return accountRepository.save(account);
    }

    @Override
    public Account put(Long id, Account account, Long customerId) {
        validator.validatePutAccount(id, account);
        Optional<Account> accountInDB = accountRepository.findById(id);
        validator.validateObject(accountInDB, id);
        account.setId(id);
        User customerFromDb = customerRepository.getReferenceById(customerId);
        account.setCustomer(customerFromDb);
        account.setAccountNumber(accountInDB.get().getAccountNumber());
        return accountRepository.save(account);
    }

    @Override
    public void remove(Long id) {
        validator.validateId(id);
        Optional<Account> account = accountRepository.findById(id);
        validator.validateObject(account, id);
        accountRepository.deleteById(id);
    }

    @Override
    public Double replenishBalance(Long id, Double amount) {
        validator.validateId(id);
        validator.validateAmount(amount);
        Optional<Account> accountFromDB = accountRepository.findById(id);
        validator.validateObject(accountFromDB, id);
        accountFromDB.get().setCurrentBalance(accountFromDB.get().getCurrentBalance() + amount);
        accountRepository.save(accountFromDB.get());
        Optional<Account> account = accountRepository.findById(id);
        return account.get().getCurrentBalance();
    }

    @Override
    public Double withdrawBalance(Long id, Double amount) {
        validator.validateId(id);
        validator.validateAmount(amount);
        Optional<Account> account = accountRepository.findById(id);
        validator.validateObject(account, id);
        validator.validateAccountInBalance(account.get());
        account.get().setCurrentBalance(account.get().getCurrentBalance() - amount);
        accountRepository.save(account.get());
        Optional<Account> accountFromDB = accountRepository.findById(id);
        return accountFromDB.get().getCurrentBalance();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendToAnotherAccount(MoneyTransfer moneyTransfer, Long from, Long to) {
        validator.validateObjectNullCase(moneyTransfer);
        moneyTransfer.setAccountFrom(findAccountByAccountNumber(String.valueOf(from)));
        moneyTransfer.setAccountTo(findAccountByAccountNumber(String.valueOf(to)));

        Optional<Account> accountFromDBFrom = accountRepository.findById(moneyTransfer.getAccountFrom().getId());
        validator.validateObject(accountFromDBFrom, from);
        Optional<Account> accountFromDBTo = accountRepository.findById(moneyTransfer.getAccountTo().getId());
        validator.validateObject(accountFromDBTo, to);
        validator.validateAccountActiveAndBalance(moneyTransfer);

        Double calculatedAmount = getCalculatedByCurrency(moneyTransfer.getAmount(),
                accountFromDBFrom.get().getCurrencyType(), accountFromDBTo.get().getCurrencyType());
        accountFromDBFrom.get().setCurrentBalance(accountFromDBFrom.get().getCurrentBalance() - moneyTransfer.getAmount());
        accountFromDBTo.get().setCurrentBalance(accountFromDBTo.get().getCurrentBalance() + calculatedAmount);

        MoneyTransfer moneyTransferUpdated = updateMoneyTransfer(moneyTransfer, accountFromDBFrom, accountFromDBTo);
        moneyTransferRepository.save(moneyTransferUpdated);
    }

    private MoneyTransfer updateMoneyTransfer(MoneyTransfer moneyTransfer, Optional<Account> accountFromDBFrom, Optional<Account> accountFromDBTo) {
        moneyTransfer.setTransferDate(LocalDateTime.now());
        moneyTransfer.setAmount(moneyTransfer.getAmount());
        moneyTransfer.setDescription(moneyTransfer.getDescription());
        moneyTransfer.setAccountTo(accountFromDBTo.get());
        moneyTransfer.setAccountFrom(accountFromDBFrom.get());
        return moneyTransfer;
    }

    @Override
    public List<MoneyTransfer> getAllTransfersOfLastYear() {
        return moneyTransferRepository.findMoneyTransferByTransferDateOfLastYear();
    }

    private Account findAccountByAccountNumber(String accountNumber) {
        Optional<Account> accountByAccountNumber = accountRepository.findAccountByAccountNumber(accountNumber);
        validator.validateAccount(accountByAccountNumber);
        return accountByAccountNumber.get();
    }

    private Double getCalculatedByCurrency(Double amount, CurrencyType currencyTypeFrom, CurrencyType currencyTypeTo) {
        if (currencyTypeFrom == CurrencyType.RUB) {
            return getCalculatedAmountFromRUB(amount, currencyTypeTo);
        } else if (currencyTypeFrom == CurrencyType.USD) {
            return getCalculatedAmountFromUSD(amount, currencyTypeTo);
        } else if (currencyTypeFrom == CurrencyType.EUR) {
            return getCalculatedAmountFromEUR(amount, currencyTypeTo);
        } else {
            return getCalculatedAmountFromAMD(amount, currencyTypeTo);
        }
    }

    private Double getCalculatedAmountFromRUB(Double amount, CurrencyType currencyTypeTo) {
        if (currencyTypeTo == CurrencyType.RUB) {
            return amount;
        } else if (currencyTypeTo == CurrencyType.AMD) {
            return amount * currencyServiceIMpl.getRUBUpdates();
        } else if (currencyTypeTo == CurrencyType.EUR) {
            return (amount * currencyServiceIMpl.getRUBUpdates()) / currencyServiceIMpl.getEURUpdates();
        } else {
            return (amount * currencyServiceIMpl.getRUBUpdates()) / currencyServiceIMpl.getUSDUpdates();
        }
    }

    private Double getCalculatedAmountFromUSD(Double amount, CurrencyType currencyTypeTo) {
        if (currencyTypeTo == CurrencyType.USD) {
            return amount;
        } else if (currencyTypeTo == CurrencyType.AMD) {
            return amount * currencyServiceIMpl.getUSDUpdates();
        } else if (currencyTypeTo == CurrencyType.EUR) {
            return (amount * currencyServiceIMpl.getUSDUpdates()) / currencyServiceIMpl.getEURUpdates();
        } else {
            return (amount * currencyServiceIMpl.getUSDUpdates()) / currencyServiceIMpl.getRUBUpdates();
        }
    }

    private Double getCalculatedAmountFromEUR(Double amount, CurrencyType currencyTypeTo) {
        if (currencyTypeTo == CurrencyType.EUR) {
            return amount;
        } else if (currencyTypeTo == CurrencyType.AMD) {
            return amount * currencyServiceIMpl.getEURUpdates();
        } else if (currencyTypeTo == CurrencyType.USD) {
            return (amount * currencyServiceIMpl.getEURUpdates()) / currencyServiceIMpl.getUSDUpdates();
        } else {
            return (amount * currencyServiceIMpl.getEURUpdates()) / currencyServiceIMpl.getRUBUpdates();
        }
    }

    private Double getCalculatedAmountFromAMD(Double amount, CurrencyType currencyTypeTo) {
        if (currencyTypeTo == CurrencyType.AMD) {
            return amount;
        } else if (currencyTypeTo == CurrencyType.USD) {
            return amount / currencyServiceIMpl.getUSDUpdates();
        } else if (currencyTypeTo == CurrencyType.RUB) {
            return amount / currencyServiceIMpl.getRUBUpdates();
        } else {
            return amount / currencyServiceIMpl.getEURUpdates();
        }
    }
}
