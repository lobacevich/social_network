package by.senla.lobacevich.messenger.dto.response;

import java.time.LocalDateTime;

public record PostDtoResponse(Long id,
                              String post,
                              LocalDateTime createdDate,
                              ProfileDtoResponse author,
                              GroupDtoResponse group) {
}
