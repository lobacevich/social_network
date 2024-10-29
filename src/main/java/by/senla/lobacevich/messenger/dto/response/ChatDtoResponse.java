package by.senla.lobacevich.messenger.dto.response;

import java.time.LocalDateTime;

public record ChatDtoResponse(Long id,
                              String name,
                              LocalDateTime createdDate) {
}
