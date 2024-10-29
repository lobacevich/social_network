package by.senla.lobacevich.messenger.dto.response;

import java.util.Set;

public record DetailedGroupDtoResponse(Long id,
                                       String name,
                                       ProfileDtoResponse owner,
                                       Set<ProfileDtoResponse> participants,
                                       Set<PostDtoResponse> posts) {
}
