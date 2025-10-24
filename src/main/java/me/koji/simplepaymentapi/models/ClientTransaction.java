package me.koji.simplepaymentapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name = "transactions")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClientTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sender;
    private Long receiver;
    private String message;
    private BigDecimal value;
    private Instant timestamp;
}
