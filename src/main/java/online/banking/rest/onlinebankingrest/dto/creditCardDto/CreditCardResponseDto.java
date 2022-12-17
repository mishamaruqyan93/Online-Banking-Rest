package online.banking.rest.onlinebankingrest.dto.creditCardDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerResponseDto;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditCardResponseDto {

    private Long id;
    private LocalDateTime expireDate;
    private StatusType statusType;
    private Integer cvv;
    private String cardNumber;
    @JsonProperty(namespace = "customerResponseDto")
    private CustomerResponseDto customerResponseDto;
}
