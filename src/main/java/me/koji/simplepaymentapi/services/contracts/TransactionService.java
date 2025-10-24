package me.koji.simplepaymentapi.services.contracts;

import me.koji.simplepaymentapi.models.ClientTransaction;
import me.koji.simplepaymentapi.models.ClientUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransactionService {
    Optional<ClientTransaction> findTransactionById(Long id);
    Page<ClientTransaction> getAllTransactions(int page, int size);
    Page<ClientTransaction> getAllTransactions(Pageable pageable);
}
