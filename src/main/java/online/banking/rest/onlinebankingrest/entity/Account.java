package online.banking.rest.onlinebankingrest.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.enums.CurrencyType;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_number", length = 16, unique = true)
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "gen_status_type")
    private StatusType genStatusType;
    @Enumerated(EnumType.STRING)
    @Column(name = "current_type")
    private CurrencyType currencyType;
    @Column(name = "current_balance")
    private Double currentBalance;
    @NotNull(message = "Customer is null")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User customer;
}
