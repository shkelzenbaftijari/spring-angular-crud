// ════════════════════════════════════════════════════════
// · USER REPOSITORY · JPA queries for User entity — supports login OR email lookup
// ════════════════════════════════════════════════════════
package com.springcrud.repository;

import com.springcrud.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);

    // JOIN FETCH — loads authorities in the same query, avoids N+1
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.authorities WHERE u.login = :login")
    Optional<User> findOneWithAuthoritiesByLogin(@Param("login") String login);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.authorities WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(@Param("email") String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.authorities")
    List<User> findAllWithAuthorities();
}
