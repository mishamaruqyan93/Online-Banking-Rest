package online.banking.rest.onlinebankingrest.dto.moneyTransferDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerMoneyTransferDto {

    private String name;
    private String surname;
}
