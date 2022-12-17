package online.banking.rest.onlinebankingrest.dto.loanDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerResponseDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanResponseDto {

    private Long id;
    private Double principalLoanAmount;
    private Double paymentAmount;
    private Date paymentDate;
    private Date dueDate;
    @JsonProperty(namespace = "customerResponseDto")
    private CustomerResponseDto customerResponseDto;
}
