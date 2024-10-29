package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.PostDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.entity.Post;

public interface PostService extends GenericService<PostDtoRequest, DetailedPostDtoResponse, Post> {
}
