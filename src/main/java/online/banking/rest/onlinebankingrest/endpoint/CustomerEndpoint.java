package online.banking.rest.onlinebankingrest.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerPostRequestDto;
import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerPutRequestDto;
import online.banking.rest.onlinebankingrest.dto.customerDto.CustomerResponseDto;
import online.banking.rest.onlinebankingrest.entity.User;
import online.banking.rest.onlinebankingrest.mapper.CustomerMapper;
import online.banking.rest.onlinebankingrest.security.CurrentUser;
import online.banking.rest.onlinebankingrest.service.impl.CustomerServiceImpl;
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
@RequestMapping("/customers")
public class CustomerEndpoint {

    private final CustomerServiceImpl customerServiceImpl;
    private final CustomerMapper customerMapper;

    @Operation(
            tags = "Customer Endpoint",
            summary = "All Customers",
            description = "Get all customers."
    )
    @GetMapping
    public List<CustomerResponseDto> findAll() {
        log.info("endpoint/findAll called by{}", new Date());
        return customerMapper.map(customerServiceImpl.getAllCustomers());
    }

    @Operation(
            tags = "Customer Endpoint",
            summary = "Get a Customer",
            description = "Get a customer by id."
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable("id") Long id) {
        Optional<User> customerById = customerServiceImpl.findCustomerById(id);
        log.info("Sent a request to search for a Customer with id by {} {}", id, LocalDateTime.now());
        return ResponseEntity.ok(customerMapper.map(customerById.get()));
    }

    @Operation(
            tags = "Customer Endpoint",
            summary = "Get a customer",
            description = "Get a customer by email"
    )
    @GetMapping("/byEmail")
    public ResponseEntity<CustomerResponseDto> findByEmail(@RequestParam("email") String email) {
        Optional<User> customerByEmail = customerServiceImpl.findCustomerByEmail(email);
        log.info("Sent a request to search for a Customer with email by {} {}", email, LocalDateTime.now());
        return ResponseEntity.ok(customerMapper.map(customerByEmail.get()));
    }

    @Operation(
            tags = "Customer Endpoint",
            summary = "Save a customer",
            description = "Saves a new customer"
    )
    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(@RequestBody CustomerPostRequestDto customerPostRequestDto,
                                                      @AuthenticationPrincipal CurrentUser currentUser) {
        customerServiceImpl.save(customerMapper.map(customerPostRequestDto));
        log.info("Customer hase bean added, phone number  by  {}", customerPostRequestDto.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

    @Operation(
            tags = "Customer Endpoint",
            summary = "Update a customer",
            description = "Updates customers all fields."
    )
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(@PathVariable("id") Long id,
                                                      @RequestBody CustomerPutRequestDto customerPutRequestDto,
                                                      @AuthenticationPrincipal CurrentUser currentUser) {
        User customer = customerServiceImpl.put(id, customerMapper.map(customerPutRequestDto));
        log.info("Customer entity modified by this id {} by {} {}", id, currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.ok(customerMapper.map(customer));
    }

    @Operation(
            tags = "Customer Endpoint",
            summary = "Update a customer",
            description = "Delete customer by id."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, @AuthenticationPrincipal CurrentUser currentUser) {
        customerServiceImpl.remove(id);
        log.info("Customer deleted by this id {} by {} {}", id, currentUser.getUser().getName() + "_" + currentUser.getUser().getSurname(), LocalDateTime.now());
        return ResponseEntity.noContent().build();
    }
}
