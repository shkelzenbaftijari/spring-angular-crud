// ════════════════════════════════════════════════════════
// · AUTHORITY · JPA entity — role record (ROLE_USER, ROLE_ADMIN)
// ════════════════════════════════════════════════════════
package com.springcrud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "authority")
public class Authority implements Serializable {

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    public Authority() {}

    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authority other)) return false;
        return name != null && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Authority{name='" + name + "'}";
    }
}
