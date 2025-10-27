package me.koji.simplepaymentapi.services;

import lombok.extern.slf4j.Slf4j;
import me.koji.simplepaymentapi.dto.AuthorizationDTO;
import me.koji.simplepaymentapi.dto.ClientTransactionDTO;
import me.koji.simplepaymentapi.exceptions.AuthenticationException;
import me.koji.simplepaymentapi.exceptions.FailedToAuthenticate;
import me.koji.simplepaymentapi.exceptions.InvalidUserException;
import me.koji.simplepaymentapi.models.ClientTransaction;
import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.repository.TransactionRepository;
import me.koji.simplepaymentapi.services.contracts.NotificationService;
import me.koji.simplepaymentapi.services.contracts.TransactionService;
import me.koji.simplepaymentapi.services.contracts.UserService;
import me.koji.simplepaymentapi.types.ClientUserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final RestTemplate restTemplate;
    private final String authURL = "https://util.devi.tools/api/v2/authorize";

    public TransactionServiceImpl(
            TransactionRepository transactionRepository, UserService userService,
            NotificationService notificationService, RestTemplate restTemplate
    ) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.restTemplate = restTemplate;
    }

    @Override
    public ClientTransaction createTransaction(
            Long id, Long sender, Long receiver,
            String message, BigDecimal value, Instant timestamp
    ) {
        final Optional<ClientUser> senderUserOptional = userService.findUserById(sender);

        senderUserOptional.ifPresent((senderUser) -> {
            if (senderUser.getType() == ClientUserType.MERCHANT)
                throw new AuthenticationException("Unable to create transaction, merchant users can't make transactions.");
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
            throw new AuthenticationException("Transaction not allowed, merchants can't make transactions.");

        sender.subtractBalance(clientTransaction.getValue());
        receiver.addBalance(clientTransaction.getValue());

        // Auth in mock, in comment due to api not working properly?.
        /*
        final ResponseEntity<AuthorizationDTO> authDTO = restTemplate.getForEntity(authURL, AuthorizationDTO.class);

        if (!authDTO.getStatusCode().is2xxSuccessful())
            throw new FailedToAuthenticate("Unable to contact authentication service, try again later.");

        final AuthorizationDTO dataDTO = authDTO.getBody();

        if (dataDTO == null)
            throw new FailedToAuthenticate("Authentication failed to return response.");

        if (dataDTO.data().authorization())
            throw new AuthenticationException("User is not authorized to perform this action.");
         */

        // To make sure that transaction can be saved before saving charge from sender balance.
        final ClientTransaction savedTransaction = transactionRepository.save(clientTransaction);

        userService.saveUser(sender);
        userService.saveUser(receiver);

        if (!notificationService.sendNotification(sender,
                MessageFormat.format("You sent {0} to {1}!", clientTransaction.getValue(), receiver.getFullName())
        ))
            log.warn("Failed to send notification to sender!");

        if (!notificationService.sendNotification(sender,
                MessageFormat.format("You received {0} from {1}!", clientTransaction.getValue(), sender.getFullName())
        ))
            log.warn("Failed to send notification to receiver!");

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

