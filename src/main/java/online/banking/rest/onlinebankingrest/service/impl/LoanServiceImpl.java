package online.banking.rest.onlinebankingrest.service.impl;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.Loan;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.repository.CustomerRepository;
import online.banking.rest.onlinebankingrest.repository.LoanRepository;
import online.banking.rest.onlinebankingrest.service.LoanService;
import online.banking.rest.onlinebankingrest.validator.OnlineBankingValidator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final CustomerRepository customerRepository;
    private final OnlineBankingValidator validator;
    private final LoanRepository loanRepository;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findLoanById(Long id) {
        validator.validateId(id);
        Optional<Loan> loan = loanRepository.findById(id);
        validator.validateLoan(loan);
        return loan.get();
    }

    @Override
    public void payLoanOff(Long id, Double amount) {
        validator.validateLoanIdAndAmount(id, amount);
        Optional<Loan> loan = loanRepository.findById(id);
        validator.validateLoan(loan);
        validator.validateLoanAmountSize(loan.get());
        loan.get().setPaymentAmount(loan.get().getPaymentAmount() - amount);
        loanRepository.save(loan.get());
    }

    @Override
    public void applyLoan(Loan loan, Long customerId) {
        validator.validateObjectNullCase(loan);
        Optional<User> customer = customerRepository.findById(customerId);
        validator.validateObject(customer, customerId);
        loan.setCustomer(customer.get());
        Loan loan1 = calculateLoan(loan);
        loanRepository.save(loan1);
    }

    private Loan calculateLoan(Loan loan) {
        validator.validateObjectNullCase(loan);
        Double paymentAmount = loan.getPaymentAmount();
        Double percent = paymentAmount / (100 * 18);
        Date dueDate = loan.getDueDate();
        int year = dueDate.getYear();
        Date currentDate = new Date();
        Double finalPercent = percent * (currentDate.getYear() - year);
        loan.setPrincipalLoanAmount(loan.getPaymentAmount() + finalPercent);
        return loan;
    }
}
