package com.example.LibraryApplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @NotBlank(message = "Phone Number is required")
    @Size(min=10, max=12)
    private String phoneNumber;

    @NotNull()
    private String role;
}
