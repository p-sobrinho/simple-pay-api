package me.koji.simplepaymentapi.mappers;

import me.koji.simplepaymentapi.dto.ClientTransactionDTO;
import me.koji.simplepaymentapi.models.ClientTransaction;

public class TransactionMapper {
    public static ClientTransaction toTransaction(ClientTransactionDTO clientTransactionDTO) {
        return new ClientTransaction(
                clientTransactionDTO.id(), clientTransactionDTO.sender(), clientTransactionDTO.receiver(),
                clientTransactionDTO.message(), clientTransactionDTO.value(), clientTransactionDTO.timestamp()
        );
    }

    public static ClientTransactionDTO toDTO(ClientTransaction clientTransaction) {
        return new ClientTransactionDTO(
                clientTransaction.getId(), clientTransaction.getSender(), clientTransaction.getReceiver(),
                clientTransaction.getMessage(), clientTransaction.getValue(), clientTransaction.getTimestamp()
        );
    }
}
