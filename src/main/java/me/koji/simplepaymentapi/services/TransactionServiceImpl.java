package me.koji.simplepaymentapi.services;

import me.koji.simplepaymentapi.models.ClientTransaction;
import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.repository.TransactionRepository;
import me.koji.simplepaymentapi.repository.UserRepository;
import me.koji.simplepaymentapi.services.contracts.TransactionService;
import me.koji.simplepaymentapi.services.contracts.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<ClientTransaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Page<ClientTransaction> getAllTransactions(int page, int size) {
        return getAllTransactions(PageRequest.of(page, size));
    }

    @Override
    public Page<ClientTransaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }
}

