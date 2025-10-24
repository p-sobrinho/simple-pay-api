package me.koji.simplepaymentapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.koji.simplepaymentapi.types.ClientUserType;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClientUser {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String cpf;
    private String password;
    private BigDecimal balance;
    private ClientUserType type;
}
