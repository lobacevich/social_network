package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfileDtoRequest(@Size(max = 23, message = "Firstname length must be less than 24")
                                String firstname,
                                @Size(max = 23, message = "Lastname length must be less than 24")
                                String lastname,
                                @NotNull(message = "User id is required")
                                Long userId) {
}
