// ════════════════════════════════════════════════════════
// · DATA INITIALIZER · Seeds roles and default admin on first startup
// ════════════════════════════════════════════════════════
package com.springcrud.config;

import com.springcrud.entity.Authority;
import com.springcrud.entity.User;
import com.springcrud.repository.AuthorityRepository;
import com.springcrud.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER  = "ROLE_USER";

    private final AuthorityRepository authorityRepository;
    private final UserRepository       userRepository;
    private final PasswordEncoder      passwordEncoder;

    public DataInitializer(AuthorityRepository authorityRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.authorityRepository = authorityRepository;
        this.userRepository      = userRepository;
        this.passwordEncoder     = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedAuthorities();
        seedAdminUser();
    }

    // · 1. AUTHORITIES ·······································

    private void seedAuthorities() {
        if (authorityRepository.findById(ROLE_ADMIN).isEmpty()) {
            authorityRepository.save(new Authority(ROLE_ADMIN));
            log.info("Created authority: {}", ROLE_ADMIN);
        }
        if (authorityRepository.findById(ROLE_USER).isEmpty()) {
            authorityRepository.save(new Authority(ROLE_USER));
            log.info("Created authority: {}", ROLE_USER);
        }
    }

    // · 2. DEFAULT ADMIN ·····································

    private void seedAdminUser() {
        if (userRepository.findOneByLogin("admin").isPresent()) {
            return;
        }

        Authority adminRole = authorityRepository.findById(ROLE_ADMIN)
            .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found after seed"));

        User admin = new User();
        admin.setLogin("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@springcrud.local");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setActivated(true);
        admin.setAuthorities(Set.of(adminRole));

        userRepository.save(admin);
        log.info("Default admin user created — login: admin / password: admin123");
    }
}
