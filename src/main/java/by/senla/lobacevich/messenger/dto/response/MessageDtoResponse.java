package by.senla.lobacevich.messenger.dto.response;

import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;

import java.time.LocalDateTime;

public record MessageDtoResponse(Long id,
                                 String message,
                                 LocalDateTime createdDate,
                                 ChatDtoRequest chat,
                                 ProfileDtoResponse author) {
}
