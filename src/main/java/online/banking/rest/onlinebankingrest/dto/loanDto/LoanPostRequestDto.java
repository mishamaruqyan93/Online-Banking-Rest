package online.banking.rest.onlinebankingrest.dto.loanDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanPostRequestDto {

    private Double principalLoanAmount;
    private Double paymentAmount;
    private Date paymentDate;
    private Date dueDate;
    private Long customerId;
}
