package me.koji.simplepaymentapi.services;

import jakarta.validation.Valid;
import lombok.extern.flogger.Flogger;
import lombok.extern.slf4j.Slf4j;
import me.koji.simplepaymentapi.dto.ClientTransactionDTO;
import me.koji.simplepaymentapi.exceptions.InvalidUserException;
import me.koji.simplepaymentapi.models.ClientTransaction;
import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.repository.TransactionRepository;
import me.koji.simplepaymentapi.repository.UserRepository;
import me.koji.simplepaymentapi.services.contracts.TransactionService;
import me.koji.simplepaymentapi.services.contracts.UserService;
import me.koji.simplepaymentapi.types.ClientUserType;
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
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Override
    public ClientTransaction createTransaction(
            Long id, Long sender, Long receiver,
            String message, BigDecimal value, Instant timestamp
    ) {
        final Optional<ClientUser> senderUserOptional = userService.findUserById(sender);
        senderUserOptional.ifPresent((senderUser) -> {
            if (senderUser.getType() == ClientUserType.MERCHANT)
                throw new IllegalArgumentException("Unable to create transaction, merchant users can't make transactions.");
        });

        if (timestamp == null) timestamp = Instant.now();

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
        final Optional<ClientUser> senderUserOptional = userService.findUserById(clientTransaction.getSender());
        final Optional<ClientUser> receiverUserOptional = userService.findUserById(clientTransaction.getReceiver());

        if (senderUserOptional.isEmpty() || receiverUserOptional.isEmpty())
            throw new InvalidUserException("Transaction can't be saved, sender user or receiver user is invalid.");

        final ClientUser sender = senderUserOptional.get();
        final ClientUser receiver = receiverUserOptional.get();

        if (sender.getType() == ClientUserType.MERCHANT)
            throw new IllegalArgumentException("Transaction not allowed, merchants can't make transactions.");

        sender.subtractBalance(clientTransaction.getValue());
        receiver.addBalance(clientTransaction.getValue());

        // To make sure that transaction can be saved before saving charge from sender balance.
        final ClientTransaction savedTransaction = transactionRepository.save(clientTransaction);

        userService.saveUser(sender);
        userService.saveUser(receiver);

        return savedTransaction;
    }

    @Override
    public void revertTransaction(ClientTransaction clientTransaction) {
        final Optional<ClientUser> senderUserOptional = userService.findUserById(clientTransaction.getSender());
        final Optional<ClientUser> receiverUserOptional = userService.findUserById(clientTransaction.getReceiver());

        senderUserOptional.ifPresentOrElse((senderUser) ->
                senderUser.addBalance(clientTransaction.getValue()),
                () -> log.warn("Unable to increase sender balance, can't find sender user.")
        );

        receiverUserOptional.ifPresentOrElse((senderUser) ->
                        senderUser.subtractBalance(clientTransaction.getValue()),
                () -> log.warn("Unable to decrease receiver balance, can't find receiver user.")
        );

        // To make sure that transaction got delete before reverting balance.
        transactionRepository.deleteById(clientTransaction.getId());

        senderUserOptional.ifPresent(userService::saveUser);
        receiverUserOptional.ifPresent(userService::saveUser);
    }
}

