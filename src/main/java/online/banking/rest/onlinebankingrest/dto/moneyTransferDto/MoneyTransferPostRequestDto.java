package online.banking.rest.onlinebankingrest.dto.moneyTransferDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MoneyTransferPostRequestDto {

    private Double amount;
    private String description;
    private Long accountFrom;
    private Long accountTo;
}