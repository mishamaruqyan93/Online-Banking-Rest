package online.banking.rest.onlinebankingrest.dto.userAuthRegDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthResponseDto {

    private String token;
    private UserDto userDto;
}
