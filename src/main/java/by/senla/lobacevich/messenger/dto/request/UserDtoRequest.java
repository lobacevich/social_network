package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDtoRequest(@NotBlank(message = "Username name is required")
                             @Size(min = 4, max = 23, message = "Username length must be between 4 and 23")
                             String username,
                             @NotBlank(message = "Email is required")
                             @Email
                             String email,
                             @NotBlank(message = "Password is required")
                             @Size(min = 4, message = "Password length must be at least 4")
                             String password) {
}
