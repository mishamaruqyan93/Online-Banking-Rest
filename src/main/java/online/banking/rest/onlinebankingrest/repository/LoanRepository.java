package online.banking.rest.onlinebankingrest.repository;

import online.banking.rest.onlinebankingrest.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
