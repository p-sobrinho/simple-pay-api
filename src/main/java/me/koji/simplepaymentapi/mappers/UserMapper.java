package me.koji.simplepaymentapi.mappers;

import me.koji.simplepaymentapi.dto.ClientUserDTO;
import me.koji.simplepaymentapi.models.ClientUser;

public class UserMapper {
    public static ClientUser toUser(ClientUserDTO clientUserDTO) {
        return new ClientUser(
                clientUserDTO.id(), clientUserDTO.firstName(), clientUserDTO.lastName(),
                clientUserDTO.email(), clientUserDTO.cpf(), clientUserDTO.password(),
                clientUserDTO.balance(), clientUserDTO.type()
        );
    }

    public static ClientUserDTO toDTO(ClientUser clientUser) {
        return new ClientUserDTO(
                clientUser.getId(), clientUser.getFirstName(), clientUser.getLastName(),
                clientUser.getEmail(), clientUser.getCpf(), clientUser.getPassword(),
                clientUser.getBalance(), clientUser.getType()
        );
    }
}
