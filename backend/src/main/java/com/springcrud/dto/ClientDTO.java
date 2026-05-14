// ════════════════════════════════════════════════════════
// · CLIENT DTO · API contract for client data transfer
// ════════════════════════════════════════════════════════
package com.springcrud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientDTO {

    private Long id;

    @NotBlank(message = "Reference number is required")
    @Size(max = 50, message = "Reference number max 50 characters")
    private String referenceNumber;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name max 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(max = 30, message = "Phone max 30 characters")
    private String phone;

    // · 1. GETTERS & SETTERS ··································

    public ClientDTO() {}

    public Long getId() { return id; }

    public ClientDTO setId(Long id) { this.id = id; return this; }

    public String getReferenceNumber() { return referenceNumber; }

    public ClientDTO setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }

    public String getFullName() { return fullName; }

    public ClientDTO setFullName(String fullName) { this.fullName = fullName; return this; }

    public String getEmail() { return email; }

    public ClientDTO setEmail(String email) { this.email = email; return this; }

    public String getPhone() { return phone; }

    public ClientDTO setPhone(String phone) { this.phone = phone; return this; }

    @Override
    public String toString() {
        return "ClientDTO{id=" + id + ", referenceNumber='" + referenceNumber + "', fullName='" + fullName + "', email='" + email + "', phone='" + phone + "'}";
    }
}
