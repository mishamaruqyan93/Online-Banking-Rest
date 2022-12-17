package online.banking.rest.onlinebankingrest.mapper;

import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerPutRequestDto;
import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerResponseDto;
import online.banking.rest.onlinebankingrest.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    User map(CustomerPostRequestDto customerPostRequestDto);

    List<CustomerResponseDto> map(List<User> customerList);

    User map(CustomerPutRequestDto customerPutRequestDto);

    CustomerResponseDto map(User customer);
}
