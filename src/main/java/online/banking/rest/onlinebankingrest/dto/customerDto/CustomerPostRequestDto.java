package online.banking.rest.onlinebankingrest.dto.customerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPostRequestDto {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
}
