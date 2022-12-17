package online.banking.rest.onlinebankingrest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.enums.StatusType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "credit_card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "expire_date")
    @CreationTimestamp
    private LocalDateTime expireDate;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status_type")
    private StatusType statusType;
    @Range(min = 0, max = 999)
    @Column(name = "cvv", length = 3)
    private Integer cvv;
    @Column(name = "card_number", length = 16, nullable = false, unique = true)
    private String cardNumber;
    @NotNull(message = "Customer is null")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User customer;
}
