package online.banking.rest.onlinebankingrest.service;

public interface CurrencyService {

    void saveUSDonCB(Double value);

    void saveEURonCB(Double value);

    void saveRUBonCB(Double value);

    Double getUSDUpdates();

    Double getRUBUpdates();

    Double getEURUpdates();
}
