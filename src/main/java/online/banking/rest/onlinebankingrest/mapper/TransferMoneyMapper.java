package online.banking.rest.onlinebankingrest.mapper;

import online.banking.rest.onlinebankingrest.dto.moneyTransferDto.MoneyTransferHistoryDto;
import online.banking.rest.onlinebankingrest.dto.moneyTransferDto.MoneyTransferPostRequestDto;
import online.banking.rest.onlinebankingrest.entity.Account;
import online.banking.rest.onlinebankingrest.entity.MoneyTransfer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransferMoneyMapper {

    MoneyTransfer map(MoneyTransferPostRequestDto moneyTransferSaveDto);

    List<MoneyTransferHistoryDto> map(List<MoneyTransfer> moneyTransfers);

    Account map(Long value);
}
