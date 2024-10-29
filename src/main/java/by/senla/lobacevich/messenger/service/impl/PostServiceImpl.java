package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.PostDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.entity.Post;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.PostMapper;
import by.senla.lobacevich.messenger.repository.PostRepository;
import by.senla.lobacevich.messenger.service.GroupService;
import by.senla.lobacevich.messenger.service.PostService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl extends AbstractService<PostDtoRequest, DetailedPostDtoResponse, Post,
        PostRepository, PostMapper> implements PostService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private GroupService groupService;

    @Autowired
    public PostServiceImpl(PostRepository repository, PostMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public DetailedPostDtoResponse createEntity(PostDtoRequest requestDto) throws EntityNotFoundException {
        Post post = mapper.dtoToEntity(requestDto);
        post.setAuthor(profileService.findEntityById(requestDto.authorId()));
        post.setGroup(groupService.findEntityById(requestDto.groupId()));
        post.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(post));
    }

    @Override
    public DetailedPostDtoResponse updateEntity(PostDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Post post = findEntityById(id);
        post.setPost(request.post() != null ? request.post() : post.getPost());
        return mapper.entityToDto(repository.save(post));
    }
}
