package by.senla.lobacevich.messenger.dto.response;

import by.senla.lobacevich.messenger.entity.enums.RequestStatus;

import java.time.LocalDateTime;

public record RequestFriendshipDtoResponse(Long id,
                                           ProfileDtoResponse sender,
                                           ProfileDtoResponse recipient,
                                           RequestStatus status,
                                           LocalDateTime updateDate) {
}
