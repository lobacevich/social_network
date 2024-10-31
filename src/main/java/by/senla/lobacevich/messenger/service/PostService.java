package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.PostDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.entity.Post;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.security.Principal;

public interface PostService extends GenericService<PostDtoRequest, DetailedPostDtoResponse, Post> {
    DetailedPostDtoResponse createEntity(PostDtoRequest request, Principal principal) throws EntityNotFoundException, AuthorizationException;
}
