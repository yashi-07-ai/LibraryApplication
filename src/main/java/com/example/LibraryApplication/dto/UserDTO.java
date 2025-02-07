package com.example.LibraryApplication.dto;

import com.example.LibraryApplication.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Name is required")
    @Size(min=3, max=100)
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
