package com.example.Library.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity(name = "users")
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "full_name")
    private String name;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "phone_number")
    @Size(max = 10)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role") // Storing a single role in the database
    private UserRole role = UserRole.MEMBER;

    @Column(name="username", unique = true)
    @Size(min=5)
    private String username;

    @Column(name="password")
    private String password;

    //List<UserRole> roles;

    private boolean enabled = true; // Default to true

    public User(){}

    // Constructor
    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.role = authorities.stream()
                .map(auth -> UserRole.valueOf(auth.getAuthority().replace("ROLE_", ""))) // Convert authority to enum
                .findFirst()
                .orElse(UserRole.MEMBER);
    }

    // Add getter & setter
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); // Convert to "ROLE_ADMIN", "ROLE_MEMBER"
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public enum UserRole {
        ADMIN, MEMBER;

        @JsonCreator
        public static UserRole fromString(String role) {
            try {
                return UserRole.valueOf(role.toUpperCase()); // Convert to uppercase if needed
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        }
    }
}
