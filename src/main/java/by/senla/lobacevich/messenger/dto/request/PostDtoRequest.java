package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostDtoRequest(@NotBlank(message = "Post text is required")
                             @Size(max = 255)
                             String post,
                             @NotNull(message = "Author id is required")
                             Long authorId,
                             @NotNull(message = "Group id is required")
                             Long groupId) {
}
