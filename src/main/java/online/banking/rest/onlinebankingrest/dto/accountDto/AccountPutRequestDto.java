package online.banking.rest.onlinebankingrest.dto.accountDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPutRequestDto {

    private String genStatusType;
    private String currencyType;
    private Double currentBalance;
    private Long customerId;
}
