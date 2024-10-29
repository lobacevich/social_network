package by.senla.lobacevich.messenger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentDtoRequest(@NotNull(message = "Post id is required")
                                Long postId,
                                @NotNull(message = "Author id is required")
                                Long authorId,
                                @NotBlank(message = "Text comment is required")
                                @Size(max = 255)
                                String textComment) {
}
