package online.banking.rest.onlinebankingrest.mapper;

import online.banking.rest.onlinebankingrest.dto.loanDto.LoanPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.loanDto.LoanResponseDto;
import online.banking.rest.onlinebankingrest.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(target = "customerResponseDto", source = "customer")
    LoanResponseDto map(Loan loan);

    Loan map(LoanPostRequestDto loanPostRequestDto);

    List<LoanResponseDto> map(List<Loan> loanList);
}
