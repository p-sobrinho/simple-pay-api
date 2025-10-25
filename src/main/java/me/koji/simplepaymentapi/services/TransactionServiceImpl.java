package me.koji.simplepaymentapi.services;

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
public class TransactionServiceImpl implements TransactionService {
    private final List<String> nullablesNotAllowed = List.of("sender", "receiver", "value", "timestamp");
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
        if (Stream.of(sender, receiver, value, timestamp).anyMatch(Objects::isNull))
            throw new IllegalArgumentException("Unable to create user one of the following parameters missing: " + nullablesNotAllowed);

        if (value == null) value = BigDecimal.ZERO;

        if (id != null) {
            if (findTransactionById(id).isPresent())
                throw new IllegalArgumentException("Unable to create transaction, id \"" + id + "\" already exists.");
        }

        final Optional<ClientUser> senderUserOptional = userService.findUserById(sender);

        senderUserOptional.ifPresent((senderUser) -> {
            if (senderUser.getType() == ClientUserType.MERCHANT)
                throw new IllegalArgumentException("Unable to create transaction, merchant users can't make transactions.");
        });

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

        // To make sure that transaction can be saved before charging from sender.
        final ClientTransaction savedTransaction = transactionRepository.save(clientTransaction);

        userService.saveUser(sender);
        userService.saveUser(receiver);

        return savedTransaction;
    }
}

