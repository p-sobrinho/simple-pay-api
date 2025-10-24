package me.koji.simplepaymentapi.services.contracts;

import me.koji.simplepaymentapi.models.ClientUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Optional<ClientUser> findUserById(Long id);
    Page<ClientUser> getAllUsers(int page, int size);
    Page<ClientUser> getAllUsers(Pageable pageable);
}
