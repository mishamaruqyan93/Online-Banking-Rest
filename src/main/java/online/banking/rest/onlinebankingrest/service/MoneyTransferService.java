package online.banking.rest.onlinebankingrest.service;

import online.banking.rest.onlinebankingrest.entity.MoneyTransfer;

import java.util.List;

public interface MoneyTransferService {

    Double replenishBalance(Long id, Double amount);

    Double withdrawBalance(Long id, Double amount);

    void sendToAnotherAccount(MoneyTransfer moneyTransfer, Long from, Long to);

    List<MoneyTransfer> getAllTransfersOfLastYear();
}
