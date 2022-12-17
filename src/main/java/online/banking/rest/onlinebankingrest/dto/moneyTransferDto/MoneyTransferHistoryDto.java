package online.banking.rest.onlinebankingrest.dto.moneyTransferDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyTransferHistoryDto {

    private Double amount;
    private String description;
    private Date transferDate;
    @JsonProperty(namespace = "accountFrom")
    private AccountMoneyTransferDto accountFrom;
    @JsonProperty(namespace = "accountTo")
    private AccountMoneyTransferDto accountTo;
}
