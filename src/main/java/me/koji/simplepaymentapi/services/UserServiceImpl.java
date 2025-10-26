package me.koji.simplepaymentapi.services;

import me.koji.simplepaymentapi.dto.ClientUserDTO;
import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.repository.UserRepository;
import me.koji.simplepaymentapi.services.contracts.UserService;
import me.koji.simplepaymentapi.types.ClientUserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ClientUser createUser(
            Long id, String firstName, String lastName,
            String email, String cpf, String password,
            BigDecimal balance, ClientUserType type
    ) {
        if (balance == null) balance = BigDecimal.ZERO;
        if (type == null) type = ClientUserType.COMMON;

        return new ClientUser(id, firstName, lastName, email, cpf, password, balance, type);
    }

    @Override
    public ClientUser createUserByDTO(ClientUserDTO clientUserDTO) {
        return createUser(
                clientUserDTO.id(), clientUserDTO.firstName(), clientUserDTO.lastName(),
                clientUserDTO.email(), clientUserDTO.cpf(), clientUserDTO.password(),
                clientUserDTO.balance(), clientUserDTO.type()
        );
    }

    @Override
    public Optional<ClientUser> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<ClientUser> getAllUsers(int page, int size) {
        return getAllUsers(PageRequest.of(page, size));
    }

    @Override
    public Page<ClientUser> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public ClientUser saveUser(ClientUser clientUser) {
        return userRepository.save(clientUser);
    }
}