package me.koji.simplepaymentapi.services;

import me.koji.simplepaymentapi.dto.ClientTransactionDTO;
import me.koji.simplepaymentapi.models.ClientTransaction;
import me.koji.simplepaymentapi.repository.TransactionRepository;
import me.koji.simplepaymentapi.services.contracts.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final List<String> nullablesNotAllowed = List.of("firstName", "lastName", "email", "cpf", "password");
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ClientTransaction createTransaction(
            Long id, Long sender, Long receiver,
            String message, BigDecimal value, Instant timestamp
    ) {
        if (Stream.of(sender, receiver, value, timestamp).anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Unable to create user one of the following parameters missing: " + nullablesNotAllowed);

        if (value == null) value = BigDecimal.ZERO;

        if (findTransactionById(id).isPresent())
            throw new IllegalArgumentException("Unable to create transaction, id \"" + id + "\" already exists.");

        return new ClientTransaction(id, sender, receiver, message, value, timestamp);
    }

    @Override
    public ClientTransaction createTransactionByDTO(ClientTransactionDTO clientTransactionDTO) {
        return createTransaction(
                clientTransactionDTO.id(), clientTransactionDTO.sender(), clientTransactionDTO.receiver(),
                clientTransactionDTO.message(), clientTransactionDTO.value(), clientTransactionDTO.timestamp()
        );
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

    @Override
    public ClientTransaction saveTransaction(ClientTransaction clientTransaction) {
        return null;
    }
}

