package by.senla.lobacevich.messenger.dto.response;

import java.time.LocalDateTime;

public record CommentDtoResponse(Long id,
                                 PostDtoResponse post,
                                 ProfileDtoResponse author,
                                 String textComment,
                                 LocalDateTime createdDate) {
}
