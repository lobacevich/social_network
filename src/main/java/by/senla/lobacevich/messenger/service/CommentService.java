package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.CommentDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.entity.Comment;

public interface CommentService extends GenericService<CommentDtoRequest, CommentDtoResponse, Comment> {
}
