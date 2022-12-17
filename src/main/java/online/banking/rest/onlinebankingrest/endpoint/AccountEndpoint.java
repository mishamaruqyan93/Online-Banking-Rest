package online.banking.rest.onlinebankingrest.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.banking.rest.onlinebankingrest.dto.accountDto.AccountPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.accountDto.AccountPutRequestDto;
import online.banking.rest.onlinebankingrest.dto.accountDto.AccountResponseDto;
import online.banking.rest.onlinebankingrest.dto.moneyTransferDto.MoneyTransferHistoryDto;
import online.banking.rest.onlinebankingrest.dto.moneyTransferDto.MoneyTransferPostRequestDto;
import online.banking.rest.onlinebankingrest.entity.Account;
import online.banking.rest.onlinebankingrest.mapper.AccountMapper;
import online.banking.rest.onlinebankingrest.mapper.TransferMoneyMapper;
import online.banking.rest.onlinebankingrest.security.CurrentUser;
import online.banking.rest.onlinebankingrest.service.impl.AccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountEndpoint {

    private final TransferMoneyMapper transferMoneyMapper;
    private final AccountServiceImpl accountServiceImpl;
    private final AccountMapper accountMapper;

    @Operation(
            tags = "Account Endpoint",
            summary = "All Accounts",
            description = "Get all accounts."
    )
    @GetMapping
    public List<AccountResponseDto> getAll() {
        log.info("endpoint/getAll called by {}", new Date());
        return accountMapper.map(accountServiceImpl.getAllAccounts());
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Get an Account",
            description = "Get an account by id."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getById(@PathVariable("id") Long id) {
        Optional<Account> accountOnDB = accountServiceImpl.findAccountById(id);
        log.info("Sent a request to search for Account with id by {} {}", id, LocalDateTime.now());
        return ResponseEntity.ok(accountMapper.map(accountOnDB.get()));
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Get an Account",
            description = "Get a account by customer id."
    )
    @GetMapping("/byCustomerId/{id}")
    public ResponseEntity<AccountResponseDto> findByAccountByCustomerId(@PathVariable("id") Long id) {
        Account byCustomerId = accountServiceImpl.findAccountByCustomerId(id);
        return ResponseEntity.ok(accountMapper.map(byCustomerId));
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Save an Account",
            description = "Get an account by customer id."
    )
    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody
                                                     AccountPostRequestDto accountPostRequestDto,
                                                     @AuthenticationPrincipal CurrentUser currentUser) {
        Account account = accountServiceImpl.save(accountMapper.map(accountPostRequestDto), accountPostRequestDto.getCustomerId());
        log.info("Account created  by {} {}", currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.ok(accountMapper.map(account));
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "update an Account",
            description = "update account by id."
    )
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> update(@PathVariable("id") Long id,
                                                     @RequestBody AccountPutRequestDto accountPutRequestDto,
                                                     @AuthenticationPrincipal CurrentUser currentUser) {
        Account account = accountServiceImpl.put(id, accountMapper.map(accountPutRequestDto), accountPutRequestDto.getCustomerId());
        log.info("Account entity modified by this id {} by {} {}", id, currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.ok(accountMapper.map(account));
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Cancel an Account",
            description = "Deletes account by id."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, @AuthenticationPrincipal CurrentUser currentUser) {
        accountServiceImpl.remove(id);
        log.info("Account deleted by this id {} by {} {}", id, currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Replenish balance",
            description = "Replenish balance and send report"
    )
    @PostMapping("/replenish-balance/{id}")
    public ResponseEntity<?> replenishBalance(@PathVariable("id") Long id, @RequestParam("amount") Double amount) {
        Double balance = accountServiceImpl.replenishBalance(id, amount);
        log.info("Money was transferred to the balance of this account by this id by {} {}", id, LocalDateTime.now());
        return ResponseEntity.ok(balance);
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Withdraw balance",
            description = "Withdraw balance and send report"
    )
    @PostMapping("/withdraw-balance/{id}")
    public ResponseEntity<?> withdrawBalance(@PathVariable("id") Long id, @RequestParam("amount") Double amount) {
        Double balanceAfterWithdrawal = accountServiceImpl.withdrawBalance(id, amount);
        log.info("Money was taken from this account so id by {} {}", id, LocalDateTime.now());
        return ResponseEntity.ok(balanceAfterWithdrawal);
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Send many",
            description = "Money is debited from one account and transferred to another account"
    )
    @PostMapping("/transfer-money")
    public ResponseEntity<?> moneyTransfer(@RequestBody MoneyTransferPostRequestDto moneyTransferPostRequestDto) {
        accountServiceImpl.sendToAnotherAccount(transferMoneyMapper.map(moneyTransferPostRequestDto),
                moneyTransferPostRequestDto.getAccountFrom(), moneyTransferPostRequestDto.getAccountTo());
        log.info("The money was transferred to an account by {} {} {}", moneyTransferPostRequestDto.getAccountFrom(),
                "To another account", moneyTransferPostRequestDto.getAccountTo());
        return ResponseEntity.ok().build();
    }

    @Operation(
            tags = "Account Endpoint",
            summary = "Transfer history",
            description = "Many transfer history of last year"
    )
    @GetMapping("/transfer-money-history")
    public ResponseEntity<List<MoneyTransferHistoryDto>> moneyTransferHistory() {
        log.info("Called the history of transfers of last year {}", LocalDateTime.now());
        return ResponseEntity.ok(transferMoneyMapper.map(accountServiceImpl.getAllTransfersOfLastYear()));
    }
}
