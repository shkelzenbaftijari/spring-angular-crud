// ════════════════════════════════════════════════════════
// · REGISTER REQUEST · Admin payload for creating a new user
// ════════════════════════════════════════════════════════
package com.springcrud.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank @Size(min = 1, max = 50) String login,
    @NotBlank @Email @Size(max = 254) String email,
    @NotBlank @Size(min = 4, max = 100) String password,
    @Size(max = 50) String firstName,
    @Size(max = 50) String lastName,
    @NotBlank String role
) {}
