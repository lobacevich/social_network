package by.senla.lobacevich.messenger.service.impl;


import by.senla.lobacevich.messenger.dto.request.CommentDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.entity.Comment;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.CommentMapper;
import by.senla.lobacevich.messenger.repository.CommentRepository;
import by.senla.lobacevich.messenger.service.CommentService;
import by.senla.lobacevich.messenger.service.PostService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl extends AbstractService<CommentDtoRequest, CommentDtoResponse, Comment,
        CommentRepository, CommentMapper> implements CommentService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PostService postService;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, CommentMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public CommentDtoResponse createEntity(CommentDtoRequest request) throws EntityNotFoundException {
        Comment comment = mapper.dtoToEntity(request);
        comment.setAuthor(profileService.findEntityById(request.authorId()));
        comment.setPost(postService.findEntityById(request.postId()));
        comment.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(comment));
    }

    @Override
    public CommentDtoResponse updateEntity(CommentDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Comment comment = findEntityById(id);
        comment.setTextComment(request.textComment() != null ? request.textComment() : comment.getTextComment());
        return mapper.entityToDto(repository.save(comment));
    }
}
