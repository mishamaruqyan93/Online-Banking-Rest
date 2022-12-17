package online.banking.rest.onlinebankingrest.repository;

import online.banking.rest.onlinebankingrest.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM currency WHERE local_date_time >= DATE(NOW() - INTERVAL 1 HOUR)   AND currency_type = 'USD'")
    List<Currency> findCurrencyByUSDLast1Hour();

    @Query(nativeQuery = true, value = "SELECT * FROM currency WHERE local_date_time >= DATE(NOW() - INTERVAL 1 HOUR)   AND currency_type = 'RUB'")
    List<Currency> findCurrencyByRUBLast1Hour();

    @Query(nativeQuery = true, value = "SELECT * FROM currency WHERE local_date_time >= DATE(NOW() - INTERVAL 1 HOUR)   AND currency_type = 'EUR'")
    List<Currency> findCurrencyByEURLast1Hour();
}
