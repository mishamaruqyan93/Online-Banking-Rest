package online.banking.rest.onlinebankingrest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "money_transfer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(name = "transfer_date")
    private LocalDateTime transferDate;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private Double amount;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_from")
    @NotNull(message = "Account is null")
    private Account accountFrom;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_to")
    @NotNull(message = "Account is  null")
    private Account accountTo;
}
