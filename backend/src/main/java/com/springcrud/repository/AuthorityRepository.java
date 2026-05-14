// ════════════════════════════════════════════════════════
// · AUTHORITY REPOSITORY · JPA repository for Authority entity
// ════════════════════════════════════════════════════════
package com.springcrud.repository;

import com.springcrud.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
