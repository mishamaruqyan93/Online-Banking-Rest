package online.banking.rest.onlinebankingrest.service;

import online.banking.rest.onlinebankingrest.entity.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();

    void payLoanOff(Long id, Double amount);

    void applyLoan(Loan loan, Long customerId);

    Loan findLoanById(Long id);
}
