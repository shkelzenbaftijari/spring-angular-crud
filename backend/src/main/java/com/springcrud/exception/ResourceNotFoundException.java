// ════════════════════════════════════════════════════════
// · RESOURCE NOT FOUND EXCEPTION · Thrown when entity is missing — maps to 404
// ════════════════════════════════════════════════════════
package com.springcrud.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
