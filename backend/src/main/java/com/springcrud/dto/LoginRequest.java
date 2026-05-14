// ════════════════════════════════════════════════════════
// · LOGIN REQUEST · Credentials payload for POST /api/auth/login
// ════════════════════════════════════════════════════════
package com.springcrud.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String login,
    @NotBlank String password
) {}
