// ════════════════════════════════════════════════════════
// · USER SERVICE · Register, list, and delete application users
// ════════════════════════════════════════════════════════
package com.springcrud.service;

import com.springcrud.dto.RegisterRequest;
import com.springcrud.dto.UserDTO;
import com.springcrud.entity.Authority;
import com.springcrud.entity.User;
import com.springcrud.repository.AuthorityRepository;
import com.springcrud.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO register(RegisterRequest req) {
        if (userRepository.findOneByLogin(req.login().toLowerCase()).isPresent()) {
            throw new IllegalStateException("Login already in use: " + req.login());
        }
        if (userRepository.findOneByEmailIgnoreCase(req.email()).isPresent()) {
            throw new IllegalStateException("Email already in use: " + req.email());
        }

        Authority authority = authorityRepository.findById(req.role())
            .orElseThrow(() -> new IllegalStateException("Role not found: " + req.role()));

        User user = new User();
        user.setLogin(req.login());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setActivated(true);
        user.setAuthorities(Set.of(authority));

        User saved = userRepository.save(user);
        log.info("Registered new user: {}", saved.getLogin());
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAllWithAuthorities().stream()
            .map(this::toDTO)
            .toList();
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("Deleted user id: {}", id);
    }

    private UserDTO toDTO(User user) {
        List<String> roles = user.getAuthorities().stream()
            .map(Authority::getName)
            .toList();
        return new UserDTO(
            user.getId(),
            user.getLogin(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.isActivated(),
            roles
        );
    }
}
