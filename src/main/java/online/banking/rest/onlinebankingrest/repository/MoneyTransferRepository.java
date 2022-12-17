package online.banking.rest.onlinebankingrest.repository;

import online.banking.rest.onlinebankingrest.entity.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM money_transfer WHERE transfer_date >= DATE(NOW() - INTERVAL 1 YEAR)")
    List<MoneyTransfer> findMoneyTransferByTransferDateOfLastYear();
}





