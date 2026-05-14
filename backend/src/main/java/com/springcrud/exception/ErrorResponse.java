// ════════════════════════════════════════════════════════
// · ERROR RESPONSE · Unified error payload returned on exceptions
// ════════════════════════════════════════════════════════
package com.springcrud.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm") LocalDateTime timestamp
) {}
