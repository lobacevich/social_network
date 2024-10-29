package by.senla.lobacevich.messenger.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DetailedPostDtoResponse(Long id,
                                      String post,
                                      LocalDateTime createdDate,
                                      ProfileDtoResponse author,
                                      GroupDtoResponse group,
                                      List<CommentDtoResponse> comments) {
}
