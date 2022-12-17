package online.banking.rest.onlinebankingrest.dto.creditCardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardPostRequestDto {

    private LocalDateTime expireDate;
    private String statusType;
    private Integer cvv;
    private String cardNumber;
    private Long customerId;
}
