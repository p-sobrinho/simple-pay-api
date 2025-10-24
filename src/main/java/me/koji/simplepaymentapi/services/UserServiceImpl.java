package me.koji.simplepaymentapi.services;

import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.repository.UserRepository;
import me.koji.simplepaymentapi.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
