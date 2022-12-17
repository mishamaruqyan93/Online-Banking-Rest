package online.banking.rest.onlinebankingrest.service.impl;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.Currency;
import online.banking.rest.onlinebankingrest.entity.enums.CurrencyType;
import online.banking.rest.onlinebankingrest.repository.CurrencyRepository;
import online.banking.rest.onlinebankingrest.service.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceIMpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public void saveUSDonCB(Double value) {
        Currency currency = createCurrency(value);
        currency.setCurrencyType(CurrencyType.USD);
        currencyRepository.save(currency);
    }

    @Override
    public void saveEURonCB(Double value) {
        Currency currency = createCurrency(value);
        currency.setCurrencyType(CurrencyType.EUR);
        currencyRepository.save(currency);
    }

    @Override
    public void saveRUBonCB(Double value) {
        Currency currency = createCurrency(value);
        currency.setCurrencyType(CurrencyType.RUB);
        currencyRepository.save(currency);
    }

    @Override
    public Double getUSDUpdates() {
        List<Currency> currencyByTransferDateOfLast12hour = currencyRepository.findCurrencyByUSDLast1Hour();
        Currency currency = CollectionUtils.lastElement(currencyByTransferDateOfLast12hour);
        return currency.getCurrency();
    }

    @Override
    public Double getRUBUpdates() {
        List<Currency> currencyByTransferDateOfLast12hour = currencyRepository.findCurrencyByRUBLast1Hour();
        Currency currency = CollectionUtils.lastElement(currencyByTransferDateOfLast12hour);
        return currency.getCurrency();
    }

    @Override
    public Double getEURUpdates() {
        List<Currency> currencyByTransferDateOfLast12hour = currencyRepository.findCurrencyByEURLast1Hour();
        Currency currency = CollectionUtils.lastElement(currencyByTransferDateOfLast12hour);
        return currency.getCurrency();
    }

    private Currency createCurrency(Double value) {
        return Currency.builder()
                .currency(value)
                .localDateTime(LocalDateTime.now())
                .build();
    }
}
