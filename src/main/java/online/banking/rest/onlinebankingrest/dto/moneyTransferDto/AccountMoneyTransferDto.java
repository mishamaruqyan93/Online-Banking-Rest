package online.banking.rest.onlinebankingrest.dto.moneyTransferDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.enums.CurrencyType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountMoneyTransferDto {

    private CurrencyType currencyType;
    @JsonProperty(namespace = "customer")
    private CustomerMoneyTransferDto customer;
}
