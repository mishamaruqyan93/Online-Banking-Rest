package online.banking.rest.onlinebankingrest.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.enums.CurrencyType;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPostRequestDto {

    private String accountNumber;
    private StatusType genStatusType;
    private CurrencyType currencyType;
    private Double currentBalance;
    private Long customerId;
}
