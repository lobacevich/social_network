package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.RequestFriendshipDtoRequest;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.RequestFriendship;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.entity.enums.RequestStatus;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.RequestFriendshipMapper;
import by.senla.lobacevich.messenger.repository.RequestFriendshipRepository;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestFriendshipServiceImplTest {

    private static final Long ID_ONE = 1L;
    private static final Long ID_ZERO = 0L;
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 0;
    private static final String USER_NAME = "UserName";
    private static final String OTHER_USER_NAME = "OtherUserName";

    @Mock
    private RequestFriendshipRepository repository;

    @Mock
    private RequestFriendshipMapper mapper;

    @Mock
    private RequestFriendshipDtoRequest requestDto;

    @Mock
    private RequestFriendshipDtoResponse responseDto;

    @Mock
    private RequestFriendship entity;

    @Mock
    private ProfileService profileService;

    @Mock
    private Profile profile;

    @Mock
    private User user;

    @Mock
    private RequestStatus status;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RequestFriendshipServiceImpl service;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createEntity_ShouldCallSaveMethodOfRepositoryAndReturnDtoResponse() throws EntityNotFoundException, InvalidDataException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(profile.getId()).thenReturn(ID_ONE);
        when(requestDto.recipientId()).thenReturn(ID_ZERO);
        when(repository.findBySenderIdAndRecipientId(profile.getId(), requestDto.recipientId())).thenReturn(Optional.empty());
        when(profileService.findEntityById(requestDto.recipientId())).thenReturn(profile);
        when(repository.save(any(RequestFriendship.class))).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        RequestFriendshipDtoResponse actual = service.createEntity(requestDto);

        verify(repository).save(any(RequestFriendship.class));
        assertEquals(responseDto, actual);
    }

    @Test
    void createEntity_ShouldThrowInvalidDataException_SentToHimself() throws EntityNotFoundException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(profile.getId()).thenReturn(ID_ONE);
        when(requestDto.recipientId()).thenReturn(ID_ONE);

        assertThrows(InvalidDataException.class, () -> service.createEntity(requestDto));
    }

    @Test
    void createEntity_ShouldThrowInvalidDataException_DuplicateRequest() throws EntityNotFoundException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);

        assertThrows(InvalidDataException.class, () -> service.createEntity(requestDto));
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

        RequestFriendshipDtoResponse actual = service.updateEntity(requestDto, ID_ONE);

        verify(repository, times(1)).save(entity);
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

        verify(repository, times(1)).deleteById(ID_ONE);
    }

    @Test
    void findAll_ShouldCallFindAllMethodOfRepository() {
        List<RequestFriendship> entityList = List.of(entity);
        List<RequestFriendshipDtoResponse> responseDtos = List.of(responseDto);
        when(repository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(new PageImpl<>(entityList));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<RequestFriendshipDtoResponse> actual = service.findAll(PAGE_SIZE, PAGE_NUMBER);

        verify(repository, times(1)).findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(responseDtos, actual);
    }

    @Test
    void isOwnerOrEmpty_ShouldReturnIsOwnerTrue() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(entity.getSender()).thenReturn(profile);
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
        when(entity.getSender()).thenReturn(profile);
        when(profile.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn(USER_NAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(OTHER_USER_NAME);
        when(entity.getRecipient()).thenReturn(profile);

        assertFalse(service.isOwnerOrEmpty(ID_ONE));
    }

    @Test
    void searchChats_ShouldReturnRequestsByStatus() throws EntityNotFoundException {
        Page<RequestFriendship> requestPage = new PageImpl<>(List.of(entity));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(profile.getId()).thenReturn(ID_ONE);
        when(repository.findByStatusAndSenderIdOrStatusAndRecipientId(status, ID_ONE, status, ID_ONE, PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(requestPage);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<RequestFriendshipDtoResponse> actual = service.searchRequests(PAGE_SIZE, PAGE_NUMBER, ID_ZERO, status);

        verify(repository, times(1)).findByStatusAndSenderIdOrStatusAndRecipientId(status, ID_ONE, status, ID_ONE, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(List.of(responseDto), actual);
    }

    @Test
    void searchChats_ShouldReturnRequestsByProfileIdAndStatus() throws EntityNotFoundException {
        Page<RequestFriendship> requestPage = new PageImpl<>(List.of(entity));
        when(repository.findByStatusAndSenderIdOrStatusAndRecipientId(status, ID_ONE, status, ID_ONE, PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(requestPage);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<RequestFriendshipDtoResponse> actual = service.searchRequests(PAGE_SIZE, PAGE_NUMBER, ID_ONE, status);

        verify(repository, times(1)).findByStatusAndSenderIdOrStatusAndRecipientId(status, ID_ONE, status, ID_ONE, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(List.of(responseDto), actual);
    }
}
