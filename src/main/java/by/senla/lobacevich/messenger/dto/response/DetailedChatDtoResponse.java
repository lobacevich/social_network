package by.senla.lobacevich.messenger.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record DetailedChatDtoResponse(Long id,
                                      String name,
                                      LocalDateTime createdDate,
                                      Set<ProfileDtoResponse> participants,
                                      List<MessageDtoResponse> messages) {
}
