package me.koji.simplepaymentapi.repository;

import me.koji.simplepaymentapi.models.ClientTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<ClientTransaction, Long> { }
