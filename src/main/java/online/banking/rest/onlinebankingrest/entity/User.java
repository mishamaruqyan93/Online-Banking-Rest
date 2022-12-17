package online.banking.rest.onlinebankingrest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.banking.rest.onlinebankingrest.entity.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name is null")
    @Column(name = "name", length = 32, nullable = false)
    private String name;
    @NotEmpty(message = "Surname is null")
    @Column(name = "surname", length = 32, nullable = false)
    private String surname;
    @NotEmpty(message = "Email is null")
    @Column(name = "email", length = 32, nullable = false)
    private String email;
    @NotEmpty(message = "Password is null")
    @Column(name = "password", nullable = false)
    private String password;
    @NotEmpty(message = "Phone number is null")
    @Column(name = "phone_number", length = 32, nullable = false)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
}
