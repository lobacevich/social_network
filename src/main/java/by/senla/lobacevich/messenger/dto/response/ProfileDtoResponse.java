package by.senla.lobacevich.messenger.dto.response;

import java.time.LocalDateTime;

public record ProfileDtoResponse(Long id,
                                 String firstname,
                                 String lastname,
                                 LocalDateTime createdDate) {
}
