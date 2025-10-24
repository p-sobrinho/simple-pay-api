package me.koji.simplepaymentapi.repository;

import me.koji.simplepaymentapi.models.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ClientUser, Long> { }
