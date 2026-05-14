// ════════════════════════════════════════════════════════
// · DOMAIN USER DETAILS SERVICE · Loads user by login OR email for Spring Security
// ════════════════════════════════════════════════════════
package com.springcrud.security;

import com.springcrud.entity.User;
import com.springcrud.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class DomainUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) {
        log.debug("Authenticating user: {}", login);
        // Supports login with username OR email
        return userRepository
            .findOneWithAuthoritiesByLogin(login.toLowerCase(Locale.ENGLISH))
            .or(() -> userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login))
            .map(this::toSpringSecurityUser)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
    }

    private org.springframework.security.core.userdetails.User toSpringSecurityUser(User user) {
        if (!user.isActivated()) {
            throw new DisabledException("Account is not activated: " + user.getLogin());
        }
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
            .map(a -> (GrantedAuthority) new SimpleGrantedAuthority(a.getName()))
            .toList();
        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            authorities
        );
    }
}
