// ════════════════════════════════════════════════════════
// · CLIENT REPOSITORY · Spring Data JPA — extends JpaRepository
// ════════════════════════════════════════════════════════
package com.springcrud.repository;

import com.springcrud.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByOrderByIdDesc();
}
