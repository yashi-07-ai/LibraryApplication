package com.example.LibraryApplication.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

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

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.MEMBER;

    public enum UserRole {
        ADMIN, MEMBER;

//        private String role;
//
//        private UserRole(String role){
//            this.role = role;
//        }

        @JsonCreator
        public static UserRole fromString(String role) {
            try {
                return UserRole.valueOf(role.toUpperCase()); // Convert to uppercase if needed
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        }

    }


//    public void setId(int id){
//        this.id = id;
//        String str = UserRole.MEMBER.toString();
//    }
//
//    public long getId(){
//        return this.id;
//    }
}
