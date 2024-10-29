package by.senla.lobacevich.messenger.dto.request;

import by.senla.lobacevich.messenger.entity.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record RequestFriendshipDtoRequest(@NotNull(message = "Sender id is required")
                                          Long senderId,
                                          @NotNull(message = "Recipient id is required")
                                          Long recipientId,
                                          RequestStatus status) {
}
