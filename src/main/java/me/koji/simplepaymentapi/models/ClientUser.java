package me.koji.simplepaymentapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.koji.simplepaymentapi.types.ClientUserType;

import java.math.BigDecimal;

@Entity @Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClientUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private String password;
    private BigDecimal balance;
    private ClientUserType type;
}
