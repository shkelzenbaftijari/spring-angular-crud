// ════════════════════════════════════════════════════════
// · CLIENT · JPA entity — client record stored in DB
// ════════════════════════════════════════════════════════
package com.springcrud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "reference_number", nullable = false, length = 50, unique = true)
    private String referenceNumber;

    @NotBlank
    @Size(max = 100)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String email;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String phone;

    // · 1. CONSTRUCTORS ·······································

    public Client() {}

    public Client(String referenceNumber, String fullName, String email, String phone) {
        this.referenceNumber = referenceNumber;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    // · 2. GETTERS & SETTERS ··································

    public Long getId() { return id; }

    public Client setId(Long id) { this.id = id; return this; }

    public String getReferenceNumber() { return referenceNumber; }

    public Client setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; return this; }

    public String getFullName() { return fullName; }

    public Client setFullName(String fullName) { this.fullName = fullName; return this; }

    public String getEmail() { return email; }

    public Client setEmail(String email) { this.email = email; return this; }

    public String getPhone() { return phone; }

    public Client setPhone(String phone) { this.phone = phone; return this; }

    @Override
    public String toString() {
        return "Client{id=" + id + ", referenceNumber='" + referenceNumber + "', fullName='" + fullName + "', email='" + email + "', phone='" + phone + "'}";
    }
}
