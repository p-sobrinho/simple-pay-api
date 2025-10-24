package me.koji.simplepaymentapi.repository;

import me.koji.simplepaymentapi.models.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ClientUser, Long> {
    //@Query("SELECT user FROM ClientUser user WHERE user.id = ?1")
    Optional<ClientUser> findById(Long id);
}
