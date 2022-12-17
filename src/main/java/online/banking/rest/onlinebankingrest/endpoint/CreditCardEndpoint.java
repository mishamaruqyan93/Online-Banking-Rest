package online.banking.rest.onlinebankingrest.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.banking.rest.onlinebankingrest.dto.creditCardDto.CreditCardPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.creditCardDto.CreditCardResponseDto;
import online.banking.rest.onlinebankingrest.entity.CreditCard;
import online.banking.rest.onlinebankingrest.mapper.CreditCardMapper;
import online.banking.rest.onlinebankingrest.security.CurrentUser;
import online.banking.rest.onlinebankingrest.service.impl.CreditCardServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/credit-cards")
public class CreditCardEndpoint {

    private final CreditCardServiceImpl creditCardServiceImpl;
    private final CreditCardMapper creditCardMapper;

    @Operation(
            tags = "Credit Cards Endpoint",
            summary = "All Credit Cards",
            description = "Get all Credit-Cards."
    )
    @GetMapping
    public List<CreditCardResponseDto> getAll() {
        log.info("endpoint/getAll called by {}", LocalDateTime.now());
        return creditCardMapper.map(creditCardServiceImpl.getAllCreditCards());
    }

    @Operation(
            tags = "Credit Card Endpoint",
            summary = "Get a credit card",
            description = "Gets a credit card by id."
    )
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardResponseDto> findCreditCardById(@PathVariable Long id) {
        Optional<CreditCard> creditCardById = creditCardServiceImpl.findCreditCardById(id);
        log.info("Sent a request to search for a credit card with id by {} {}", id, LocalDateTime.now());
        return ResponseEntity.ok(creditCardMapper.map(creditCardById.get()));
    }

    @Operation(
            tags = "Credit Card Endpoint",
            summary = "Save Credit Card",
            description = "Save Credit Card."
    )
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreditCardPostRequestDto creditCardPostRequestDto,
                                    @AuthenticationPrincipal CurrentUser currentUser) {
        creditCardServiceImpl.saveCreditCard(creditCardMapper.map(creditCardPostRequestDto), creditCardPostRequestDto.getCustomerId());
        log.info("Credit Card created  by {} {}",
                currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> cancelCreditCard(@PathVariable("id") Long id, @AuthenticationPrincipal CurrentUser currentUser) {
        creditCardServiceImpl.cancelCreditCard(id);
        log.info("The credit card has canceled this id {} by {} {}", id, currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.ok().build();
    }
}
