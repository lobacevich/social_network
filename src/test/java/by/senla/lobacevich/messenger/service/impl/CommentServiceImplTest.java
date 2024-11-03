package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.CommentDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.entity.Comment;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.entity.Post;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.CommentMapper;
import by.senla.lobacevich.messenger.repository.CommentRepository;
import by.senla.lobacevich.messenger.service.PostService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private static final Long ID_ONE = 1L;
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 0;
    private static final String USER_NAME = "UserName";
    private static final String OTHER_USER_NAME = "OtherUserName";

    @Mock
    private CommentRepository repository;

    @Mock
    private CommentMapper mapper;

    @Mock
    private CommentDtoRequest requestDto;

    @Mock
    private CommentDtoResponse responseDto;

    @Mock
    private Comment entity;

    @Mock
    private ProfileService profileService;

    @Mock
    private Profile profile;

    @Mock
    private PostService postService;

    @Mock
    private Post post;

    @Mock
    private Group group;

    @Mock
    private User user;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentServiceImpl service;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createEntity_ShouldCallSaveMethodOfRepositoryAndReturnDtoResponse() throws AccessDeniedException, EntityNotFoundException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(postService.findEntityById(requestDto.postId())).thenReturn(post);
        when(post.getGroup()).thenReturn(group);
        when(group.getParticipants()).thenReturn(Set.of(profile));
        when(mapper.dtoToEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        CommentDtoResponse actual = service.createEntity(requestDto);

        verify(repository).save(entity);
        assertEquals(responseDto, actual);
    }

    @Test
    void createEntity_ShouldThrowAccessDeniedException() throws EntityNotFoundException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(postService.findEntityById(requestDto.postId())).thenReturn(post);
        when(post.getGroup()).thenReturn(group);
        when(group.getParticipants()).thenReturn(Set.of());

        assertThrows(AccessDeniedException.class, () -> service.createEntity(requestDto));
    }

    @Test
    void findById_ShouldReturnDtoResponse() throws EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        CommentDtoResponse actual = service.findById(ID_ONE);

        assertEquals(responseDto, actual);
    }

    @Test
    void findById_ShouldThrowException() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(ID_ONE));
    }

    @Test
    void updateEntity_ShouldCallSaveMethodOfRepositoryAndReturnDtoResponse() throws InvalidDataException, EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        CommentDtoResponse actual = service.updateEntity(requestDto, ID_ONE);

        verify(repository).save(entity);
        assertEquals(responseDto, actual);
    }

    @Test
    void updateEntity_ShouldThrowException() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateEntity(requestDto, ID_ONE));
    }

    @Test
    void deleteEntity_ShouldCallDeleteByIdMethodOfRepository() throws EntityNotFoundException {
        service.deleteUserAndProfile(ID_ONE);

        verify(repository).deleteById(ID_ONE);
    }

    @Test
    void findAll_ShouldCallFindAllMethodOfRepository() {
        List<Comment> entityList = List.of(entity);
        List<CommentDtoResponse> responseDtos = List.of(responseDto);
        when(repository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(new PageImpl<>(entityList));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<CommentDtoResponse> actual = service.findAll(PAGE_SIZE, PAGE_NUMBER);

        verify(repository, times(1)).findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(responseDtos, actual);
    }

    @Test
    void isOwnerOrEmpty_ShouldReturnIsOwnerTrue() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(entity.getAuthor()).thenReturn(profile);
        when(profile.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn(USER_NAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);

        assertTrue(service.isOwnerOrEmpty(ID_ONE));
    }

    @Test
    void isOwnerOrEmpty_ShouldReturnIsEmptyTrue() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.empty());

        assertTrue(service.isOwnerOrEmpty(ID_ONE));
    }

    @Test
    void isOwnerOrEmpty_ShouldReturnFalse() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(entity.getAuthor()).thenReturn(profile);
        when(profile.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn(USER_NAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(OTHER_USER_NAME);

        assertFalse(service.isOwnerOrEmpty(ID_ONE));
    }
}