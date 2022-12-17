package online.banking.rest.onlinebankingrest.mapper;

import online.banking.rest.onlinebankingrest.dto.accountDto.AccountPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.accountDto.AccountPutRequestDto;
import online.banking.rest.onlinebankingrest.dto.accountDto.AccountResponseDto;
import online.banking.rest.onlinebankingrest.entity.Account;
import online.banking.rest.onlinebankingrest.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account map(AccountPostRequestDto accountPostRequestDto);

    List<AccountResponseDto> map(List<Account> accountList);


    Account map(AccountPutRequestDto accountPutRequestDto);

    AccountResponseDto map(Optional<Account> account);

    @Mapping(target = "customerResponseDto", source = "customer")
    AccountResponseDto map(Account account);

    User map(Long value);
}
