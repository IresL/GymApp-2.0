package com.gym.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "ux_users_username", columnList = "username", unique = true)
})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(name="first_name", nullable=false, length=100)
    private String firstName;

    @NotBlank @Column(name="last_name", nullable=false, length=100)
    private String lastName;

    @NotBlank @Column(name="username", nullable=false, length=150, unique=true)
    private String username;

    @NotBlank @Column(name="password_hash", nullable=false, length=255)
    private String passwordHash;

    @NotNull @Column(name="is_active", nullable=false)
    private Boolean isActive;

    // getters/setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; } public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; } public void setLastName(String lastName) { this.lastName = lastName; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean active) { isActive = active; }

    @Override public boolean equals(Object o){ return this==o || (o instanceof User u && Objects.equals(id,u.id)); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
