package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.CommentDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.entity.Comment;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.security.Principal;

public interface CommentService extends GenericService<CommentDtoRequest, CommentDtoResponse, Comment> {
    CommentDtoResponse createEntity(CommentDtoRequest request, Principal principal) throws EntityNotFoundException, AuthorizationException;
}
