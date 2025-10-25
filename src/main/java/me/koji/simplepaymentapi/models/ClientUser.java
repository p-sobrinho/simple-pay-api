package me.koji.simplepaymentapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.koji.simplepaymentapi.types.ClientUserType;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@Entity @Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClientUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String cpf;

    private String password;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private ClientUserType type;

    @Override
    public final boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof ClientUser otherClient)) return false;

        return Objects.equals(this.id, otherClient.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.id);
    }
}
