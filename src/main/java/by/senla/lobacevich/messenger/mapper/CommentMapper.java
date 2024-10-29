package by.senla.lobacevich.messenger.mapper;


import by.senla.lobacevich.messenger.dto.request.CommentDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.entity.Comment;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper extends GenericMapper<CommentDtoRequest, CommentDtoResponse, Comment> {
}
