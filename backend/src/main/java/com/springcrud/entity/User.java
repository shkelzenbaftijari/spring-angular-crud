// ════════════════════════════════════════════════════════
// · USER · JPA entity — application user with roles
// ════════════════════════════════════════════════════════
package com.springcrud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springcrud.config.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "app_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    // NOT NULL — required for password reset flow
    @NotNull
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    // @JsonIgnore — never exposed in API responses
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    // reset_date — used to expire reset links (e.g. after 24h)
    @Column(name = "reset_date")
    private Instant resetDate;

    @Column(name = "created_date", updatable = false, nullable = false)
    private Instant createdDate;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
    )
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdDate = Instant.now();
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }

    // Always stored lowercase — consistent login regardless of input case
    public void setLogin(String login) {
        this.login = login != null ? login.toLowerCase(Locale.ENGLISH) : null;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActivated() { return activated; }
    public void setActivated(boolean activated) { this.activated = activated; }

    public String getActivationKey() { return activationKey; }
    public void setActivationKey(String activationKey) { this.activationKey = activationKey; }

    public String getResetKey() { return resetKey; }
    public void setResetKey(String resetKey) { this.resetKey = resetKey; }

    public Instant getResetDate() { return resetDate; }
    public void setResetDate(Instant resetDate) { this.resetDate = resetDate; }

    public Instant getCreatedDate() { return createdDate; }

    public Set<Authority> getAuthorities() { return authorities; }
    public void setAuthorities(Set<Authority> authorities) { this.authorities = authorities; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{login='" + login + "', email='" + email + "', activated=" + activated + "}";
    }
}
