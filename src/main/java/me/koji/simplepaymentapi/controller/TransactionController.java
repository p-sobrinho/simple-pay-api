package me.koji.simplepaymentapi.controller;

import jakarta.validation.Valid;
import me.koji.simplepaymentapi.dto.ClientTransactionDTO;
import me.koji.simplepaymentapi.exceptions.EqualsUserTransactionException;
import me.koji.simplepaymentapi.exceptions.InvalidTransactionException;
import me.koji.simplepaymentapi.exceptions.InvalidUserException;
import me.koji.simplepaymentapi.mappers.TransactionMapper;
import me.koji.simplepaymentapi.models.ClientTransaction;
import me.koji.simplepaymentapi.services.contracts.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientTransactionDTO> getTransaction(@PathVariable Long id) {
        final Optional<ClientTransaction> queryTransaction = transactionService.findTransactionById(id);

        if (queryTransaction.isEmpty())
            throw new InvalidTransactionException("Unable to find transaction with id: {0}.", id);

        return ResponseEntity.ok(TransactionMapper.toDTO(queryTransaction.get()));
    }

    @GetMapping
    public ResponseEntity<Page<ClientTransactionDTO>> getAllTransactions(@RequestBody Pageable pageable) {
        return ResponseEntity.ok(transactionService.getAllTransactions(pageable).map(TransactionMapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<ClientTransactionDTO> createTransaction(@RequestBody @Valid ClientTransactionDTO transactionDTO) {
        if (Objects.equals(transactionDTO.sender(), transactionDTO.receiver()))
            throw new EqualsUserTransactionException(
                    "Sender and receiver are the same user, doesn't make sense send money to itself."
            );

        final ClientTransaction clientTransaction = transactionService.createTransactionByDTO(transactionDTO);
        final ClientTransaction savedTransaction = transactionService.saveTransaction(clientTransaction);

        return ResponseEntity.ok(TransactionMapper.toDTO(savedTransaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        final Optional<ClientTransaction> queryTransaction = transactionService.findTransactionById(id);

        if (queryTransaction.isEmpty())
            throw new InvalidTransactionException("Unable to find transaction with id: {0}.", id);

        transactionService.revertTransaction(queryTransaction.get());

        return ResponseEntity.ok("Transaction with id: " + id + " has been deleted and reverted.");
    }
}
