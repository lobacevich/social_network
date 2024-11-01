package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatDtoRequest(@Size(max = 23, message = "Name length must be not more than 23")
                             String name,
                             @NotNull(message = "Partner id is required")
                             Long partnerId) {
}
