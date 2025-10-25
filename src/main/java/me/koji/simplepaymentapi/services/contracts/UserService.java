package me.koji.simplepaymentapi.services.contracts;

import me.koji.simplepaymentapi.dto.ClientUserDTO;
import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.types.ClientUserType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {
    ClientUser createUser(
            Long id, String firstName, String lastName,
            String email, String cpf, String password,
            BigDecimal balance, ClientUserType type
    );
    ClientUser createUserByDTO(ClientUserDTO clientUserDTO);
    Optional<ClientUser> findUserById(Long id);
    Page<ClientUser> getAllUsers(int page, int size);
    Page<ClientUser> getAllUsers(Pageable pageable);
    ClientUser saveUser(ClientUser clientUser);
}
