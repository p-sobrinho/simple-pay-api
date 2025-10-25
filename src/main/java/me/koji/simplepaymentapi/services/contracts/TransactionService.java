package me.koji.simplepaymentapi.services.contracts;

import me.koji.simplepaymentapi.dto.ClientTransactionDTO;
import me.koji.simplepaymentapi.models.ClientTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface TransactionService {
    ClientTransaction createTransaction(
            Long id, Long sender, Long receiver,
            String message, BigDecimal value, Instant timestamp
    );
    ClientTransaction createTransactionByDTO(ClientTransactionDTO clientTransactionDTO);
    Optional<ClientTransaction> findTransactionById(Long id);
    Page<ClientTransaction> getAllTransactions(int page, int size);
    Page<ClientTransaction> getAllTransactions(Pageable pageable);

    ClientTransaction saveTransaction(ClientTransaction clientTransaction);
}
