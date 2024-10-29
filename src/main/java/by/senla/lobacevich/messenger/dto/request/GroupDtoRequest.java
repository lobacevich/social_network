package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GroupDtoRequest(@NotBlank(message = "Group name is required")
                              @Size(max = 23, message = "Group name must be less than 24")
                              String name,
                              @NotNull(message = "Owner id is required")
                              Long ownerId) {
}
