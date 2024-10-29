package by.senla.lobacevich.messenger.dto.response;

import by.senla.lobacevich.messenger.entity.enums.Role;

public record UserDtoResponse(Long id,
                              String username,
                              String email,
                              Role role) {
}
