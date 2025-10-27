package me.koji.simplepaymentapi.mappers;

import me.koji.simplepaymentapi.dto.ClientUserDTO;
import me.koji.simplepaymentapi.models.ClientUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public ClientUser toUser(ClientUserDTO clientUserDTO) {
        return new ClientUser(
                clientUserDTO.id(), clientUserDTO.firstName(), clientUserDTO.lastName(),
                clientUserDTO.email(), clientUserDTO.cpf(), clientUserDTO.password(),
                clientUserDTO.balance(), clientUserDTO.type()
        );
    }

    public ClientUserDTO toDTO(ClientUser clientUser) {
        return new ClientUserDTO(
                clientUser.getId(), clientUser.getFirstName(), clientUser.getLastName(),
                clientUser.getEmail(), clientUser.getCpf(), clientUser.getPassword(),
                clientUser.getBalance(), clientUser.getType()
        );
    }
}
