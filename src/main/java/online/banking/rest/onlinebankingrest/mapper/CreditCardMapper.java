package online.banking.rest.onlinebankingrest.mapper;

import online.banking.rest.onlinebankingrest.dto.creditCardDto.CreditCardPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.creditCardDto.CreditCardResponseDto;
import online.banking.rest.onlinebankingrest.entity.CreditCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {

    CreditCardResponseDto map(Optional<CreditCard> creditCard);

    CreditCard map(CreditCardPostRequestDto creditCardPostRequestDto);

    @Mapping(target = "customerResponseDto", source = "customer")
    CreditCardResponseDto map(CreditCard creditCard);

    List<CreditCardResponseDto> map(List<CreditCard> creditCardList);
}
