package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.PostDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.entity.Post;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
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
    public DetailedPostDtoResponse createEntity(PostDtoRequest request) throws EntityNotFoundException, AuthorizationException {
        validateIsInGroup(request);
        Post post = mapper.dtoToEntity(request);
        post.setAuthor(profileService.findEntityById(request.authorId()));
        post.setGroup(groupService.findEntityById(request.groupId()));
        post.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(post));
    }

    private void validateIsInGroup(PostDtoRequest request) throws EntityNotFoundException, AuthorizationException {
        Profile profile = profileService.findEntityById(request.authorId());
        Group group = groupService.findEntityById(request.groupId());
        if (!group.getParticipants().contains(profile)) {
            throw new AuthorizationException("Profile id " + profile.getId() + " is not in group id " + group.getId());
        }
    }

    @Override
    public DetailedPostDtoResponse updateEntity(PostDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Post post = findEntityById(id);
        post.setPost(request.post() != null ? request.post() : post.getPost());
        return mapper.entityToDto(repository.save(post));
    }
}
