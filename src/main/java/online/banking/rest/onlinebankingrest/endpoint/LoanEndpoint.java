package online.banking.rest.onlinebankingrest.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.banking.rest.onlinebankingrest.dto.loanDto.LoanPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.loanDto.LoanResponseDto;
import online.banking.rest.onlinebankingrest.entity.Loan;
import online.banking.rest.onlinebankingrest.mapper.LoanMapper;
import online.banking.rest.onlinebankingrest.security.CurrentUser;
import online.banking.rest.onlinebankingrest.service.impl.LoanServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanEndpoint {

    private final LoanServiceImpl loanServiceImpl;
    private final LoanMapper loanMapper;

    @Operation(
            tags = "Loan Endpoint",
            summary = "All Loans",
            description = "Get all loans."
    )
    @GetMapping
    public List<LoanResponseDto> getAll() {
        log.info("endpoint/getAll called by {}", LocalDateTime.now());
        return loanMapper.map(loanServiceImpl.getAllLoans());
    }

    @Operation(
            tags = "Loan Endpoint",
            summary = "Get a Loan",
            description = "Get a loan by id."
    )
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDto> getLoanById(@PathVariable("id") Long id) {
        Loan loanById = loanServiceImpl.findLoanById(id);
        log.info("Sent a request to search for a Loan with id by {} {}", id, LocalDateTime.now());
        return ResponseEntity.ok(loanMapper.map(loanById));
    }

    @Operation(
            tags = "Loan Endpoint",
            summary = "Apply loan.",
            description = "Apply for a loan."
    )
    @PostMapping("/apply-loan")
    public ResponseEntity<?> applyLoan(@RequestBody LoanPostRequestDto loanPostRequestDto,
                                       @AuthenticationPrincipal CurrentUser currentUser) {
        loanServiceImpl.applyLoan(loanMapper.map(loanPostRequestDto), loanPostRequestDto.getCustomerId());
        log.info("Loan was created by {} {}", currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.status(CREATED).build();
    }

    @Operation(
            tags = "Loan Endpoint",
            summary = "Pay loan off",
            description = "Pay the remaining amount and close the loan."
    )
    @DeleteMapping("/pay-loan-off/{id}")
    public ResponseEntity<?> loanPayment(@PathVariable("id") Long id, @RequestParam("amount") Double amount,
                                         @AuthenticationPrincipal CurrentUser currentUser) {
        loanServiceImpl.payLoanOff(id, amount);
        log.info("Loan deleted by this id {} by {} {}", id, currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.ok().build();
    }
}
