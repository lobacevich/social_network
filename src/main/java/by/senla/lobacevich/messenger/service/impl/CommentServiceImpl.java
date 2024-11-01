package by.senla.lobacevich.messenger.service.impl;


import by.senla.lobacevich.messenger.dto.request.CommentDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.entity.Comment;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.CommentMapper;
import by.senla.lobacevich.messenger.repository.CommentRepository;
import by.senla.lobacevich.messenger.security.SecurityUtils;
import by.senla.lobacevich.messenger.service.CommentService;
import by.senla.lobacevich.messenger.service.PostService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl extends AbstractService<CommentDtoRequest, CommentDtoResponse, Comment,
        CommentRepository, CommentMapper> implements CommentService {

    private final ProfileService profileService;
    private final PostService postService;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, CommentMapper mapper, ProfileService profileService, PostService postService) {
        super(repository, mapper);
        this.profileService = profileService;
        this.postService = postService;
    }

    @Override
    public CommentDtoResponse createEntity(CommentDtoRequest request) throws EntityNotFoundException, AccessDeniedException {
        Profile profile = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        validateIsInGroup(request, profile);
        Comment comment = mapper.dtoToEntity(request);
        comment.setAuthor(profile);
        comment.setPost(postService.findEntityById(request.postId()));
        comment.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(comment));
    }

    private void validateIsInGroup(CommentDtoRequest request, Profile profile) throws EntityNotFoundException, AccessDeniedException {
        Group group = postService.findEntityById(request.postId()).getGroup();
        if (!group.getParticipants().contains(profile)) {
            throw new AccessDeniedException("Profile id " + profile.getId() + " is not in group id " + group.getId());
        }
    }

    @Override
    public CommentDtoResponse updateEntity(CommentDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Comment comment = findEntityById(id);
        comment.setTextComment(request.textComment() != null ? request.textComment() : comment.getTextComment());
        return mapper.entityToDto(repository.save(comment));
    }

    public boolean isOwnerOrEmpty(Long id) {
        return repository.findById(id)
                .map(comment -> comment.getAuthor().getUser().getUsername().equals(SecurityUtils.getAuthenticatedUsername()))
                .orElse(true);
    }
}
