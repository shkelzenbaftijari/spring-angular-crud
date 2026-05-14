// ════════════════════════════════════════════════════════
// · USER DTO · User API response — exposes safe fields only
// ════════════════════════════════════════════════════════
package com.springcrud.dto;

import java.util.List;

public record UserDTO(
    Long id,
    String login,
    String firstName,
    String lastName,
    String email,
    boolean activated,
    List<String> roles
) {}
