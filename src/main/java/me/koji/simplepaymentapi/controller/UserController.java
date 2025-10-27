package me.koji.simplepaymentapi.controller;

import jakarta.validation.Valid;
import me.koji.simplepaymentapi.dto.ClientUserDTO;
import me.koji.simplepaymentapi.exceptions.InvalidUserException;
import me.koji.simplepaymentapi.mappers.UserMapper;
import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.services.contracts.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientUserDTO> getUser(@PathVariable Long id) {
        final Optional<ClientUser> queryUser = userService.findUserById(id);

        if (queryUser.isEmpty())
            throw new InvalidUserException("Unable to find user with id: {0}.", id);

        return ResponseEntity.ok(userMapper.toDTO(queryUser.get()));
    }

    @GetMapping
    public ResponseEntity<Page<ClientUserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable).map(userMapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<ClientUserDTO> createUser(@RequestBody @Valid ClientUserDTO userDTO) {
        final ClientUser clientUser = userService.createUserByDTO(userDTO);
        final ClientUser savedUser = userService.saveUser(clientUser);

        return ResponseEntity.ok(userMapper.toDTO(savedUser));
    }
}
