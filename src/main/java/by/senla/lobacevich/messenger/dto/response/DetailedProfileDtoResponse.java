package by.senla.lobacevich.messenger.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record DetailedProfileDtoResponse(Long id,
                                         String username,
                                         String firstname,
                                         String lastname,
                                         LocalDateTime createdDate,
                                         UserDtoResponse user,
                                         Set<GroupDtoResponse> ownedGroups,
                                         Set<GroupDtoResponse> groups,
                                         Set<ChatDtoResponse> chats) {
}
