package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageDtoRequest(@NotBlank(message = "Text message is required")
                                @Size(max = 255)
                                String message,
                                @NotNull(message = "Chat is is required")
                                Long chatId) {
}
