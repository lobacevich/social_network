package by.senla.lobacevich.messenger.mapper;

import by.senla.lobacevich.messenger.dto.request.PostDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.entity.Post;
import org.mapstruct.Mapper;

@Mapper
public interface PostMapper extends GenericMapper<PostDtoRequest, DetailedPostDtoResponse, Post> {
}
