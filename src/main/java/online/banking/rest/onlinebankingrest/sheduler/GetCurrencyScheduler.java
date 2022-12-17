package online.banking.rest.onlinebankingrest.sheduler;

import lombok.RequiredArgsConstructor;
import online.banking.rest.onlinebankingrest.service.impl.CurrencyServiceIMpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetCurrencyScheduler {

    private final CurrencyServiceIMpl currencyServiceIMpl;
    private final RestTemplate restTemplate;

    @Value("${cb.url}")
    private String cbUrl;

    @Scheduled(cron = "* 0 * * * *")
    public void getUSDCurrencyFromCB() {
        ResponseEntity<HashMap> currency = restTemplate.getForEntity(cbUrl + "?currency=USD", HashMap.class);
        HashMap<String, Double> hashMap = currency.getBody();
        List<String> listOfUSD = new ArrayList<>();
        if (!hashMap.isEmpty()) {
            for (String s : hashMap.keySet()) {
                listOfUSD.add(String.valueOf(hashMap.get(s)));
            }
        }
        currencyServiceIMpl.saveUSDonCB(Double.parseDouble(Objects.requireNonNull(CollectionUtils.lastElement(listOfUSD))));
    }

    @Scheduled(cron = "* 0 * * * *")
    public void getEURCurrencyFromCB() {
        ResponseEntity<HashMap> currency = restTemplate.getForEntity(cbUrl + "?currency=EUR", HashMap.class);
        HashMap<String, Double> hashMap = currency.getBody();
        List<String> listOfEUR = new ArrayList<>();
        if (!hashMap.isEmpty()) {
            for (String s : hashMap.keySet()) {
                listOfEUR.add(String.valueOf(hashMap.get(s)));
            }
        }
        currencyServiceIMpl.saveEURonCB(Double.parseDouble(Objects.requireNonNull(CollectionUtils.lastElement(listOfEUR))));
    }

    @Scheduled(cron = "* 0 * * * *")
    public void getRUBCurrencyFromCB() {
        ResponseEntity<HashMap> currency = restTemplate.getForEntity(cbUrl + "?currency=RUB", HashMap.class);
        HashMap<String, Double> hashMap = currency.getBody();
        List<String> listOfRUB = new ArrayList<>();
        if (!hashMap.isEmpty()) {
            for (String s : hashMap.keySet()) {
                listOfRUB.add(String.valueOf(hashMap.get(s)));
            }
        }
        currencyServiceIMpl.saveRUBonCB(Double.parseDouble(Objects.requireNonNull(CollectionUtils.lastElement(listOfRUB))));
    }
}




