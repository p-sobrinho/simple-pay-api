package me.koji.simplepaymentapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity @Table(name = "transactions")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClientTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sender;
    @Column(nullable = false)
    private Long receiver;

    private String message;

    @Column(nullable = false)
    @DecimalMin(value = "1.0", message = "Transaction value can't be lower than 1.0.")
    private BigDecimal value;

    @Column(nullable = false)
    private Instant timestamp;

    @Override
    public final boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof ClientTransaction otherTransaction)) return false;

        return Objects.equals(this.id, otherTransaction.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.id);
    }
}
