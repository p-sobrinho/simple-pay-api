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

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String cpf;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private ClientUserType type = ClientUserType.COMMON; //Default value

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
