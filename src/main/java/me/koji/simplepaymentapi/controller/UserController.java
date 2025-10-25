package me.koji.simplepaymentapi.controller;

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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientUserDTO> getUser(@PathVariable Long id) {
        final Optional<ClientUser> queryUser = userService.findUserById(id);

        if (queryUser.isEmpty())
            throw new InvalidUserException("Unable to find user with id: {}.", id);

        return ResponseEntity.ok(UserMapper.toDTO(queryUser.get()));
    }

    @GetMapping
    public ResponseEntity<Page<ClientUserDTO>> getAllUsers(@RequestBody Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable).map(UserMapper::toDTO));
    }

    @PostMapping
    public ResponseEntity<ClientUserDTO> createUser(@RequestBody ClientUserDTO userDTO) {
        final ClientUser clientUser = userService.createUserByDTO(userDTO);
        final ClientUser savedUser = userService.saveUser(clientUser);

        return ResponseEntity.ok(UserMapper.toDTO(savedUser));
    }
}
