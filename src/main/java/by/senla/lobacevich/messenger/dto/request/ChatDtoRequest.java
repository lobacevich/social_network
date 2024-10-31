package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatDtoRequest(@NotBlank(message = "Chat name is required")
                             @Size(max = 23, message = "Name length must be not more than 23")
                             String name,
                             @NotNull(message = "Owner id is required")
                             Long ownerId) {

}
