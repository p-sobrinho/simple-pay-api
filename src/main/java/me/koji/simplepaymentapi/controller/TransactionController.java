package me.koji.simplepaymentapi.controller;

import jakarta.validation.Valid;
import me.koji.simplepaymentapi.dto.ClientTransactionDTO;
import me.koji.simplepaymentapi.exceptions.InvalidUserException;
import me.koji.simplepaymentapi.mappers.TransactionMapper;
import me.koji.simplepaymentapi.models.ClientTransaction;
import me.koji.simplepaymentapi.services.contracts.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        final Optional<ClientTransaction> queryUser = transactionService.findTransactionById(id);

        if (queryUser.isEmpty())
            throw new InvalidUserException("Unable to find transaction with id: {0}.", id);

        return ResponseEntity.ok(TransactionMapper.toDTO(queryUser.get()));
    }

    @GetMapping
    public ResponseEntity<Page<ClientTransactionDTO>> getAllTransactions(@RequestBody Pageable pageable) {
        return ResponseEntity.ok(transactionService.getAllTransactions(pageable).map(TransactionMapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<ClientTransactionDTO> createTransaction(@RequestBody @Valid ClientTransactionDTO userDTO) {
        final ClientTransaction clientTransaction = transactionService.createTransactionByDTO(userDTO);
        final ClientTransaction savedTransaction = transactionService.saveTransaction(clientTransaction);

        return ResponseEntity.ok(TransactionMapper.toDTO(savedTransaction));
    }
}
