package me.koji.simplepaymentapi.services.contracts;

import jakarta.annotation.Nullable;
import me.koji.simplepaymentapi.models.ClientUser;

import java.util.Optional;

public interface UserService {
    Optional<ClientUser> findUserById(Long id);
}
