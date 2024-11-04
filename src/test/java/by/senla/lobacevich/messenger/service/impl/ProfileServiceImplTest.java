package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.mapper.ProfileMapper;
import by.senla.lobacevich.messenger.repository.ChatProfileRepository;
import by.senla.lobacevich.messenger.repository.GroupParticipantRepository;
import by.senla.lobacevich.messenger.repository.ProfileRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    private static final Long ID_ONE = 1L;
    private static final Long ID_TWO = 2L;
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 0;
    private static final String USER_NAME = "UserName";

    @Mock
    private ProfileRepository repository;

    @Mock
    private ProfileMapper mapper;

    @Mock
    private ProfileDtoRequest requestDto;

    @Mock
    private DetailedProfileDtoResponse responseDto;

    @Mock
    private Profile entity;

    @Mock
    private ChatProfileRepository chatProfileRepository;

    @Mock
    private GroupParticipantRepository groupParticipantRepository;

    @Mock
    private User user;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ProfileServiceImpl service;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createEntity_ShouldCallCreateMethodAndReturnProfileDtoResponse() {
        when(repository.save(any(Profile.class))).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        DetailedProfileDtoResponse actual = service.createEntity(user);

        verify(repository, times(1)).save(any(Profile.class));
        assertEquals(responseDto, actual);
    }

    @Test
    void findById_ShouldReturnDtoResponse() throws EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        DetailedProfileDtoResponse actual = service.findById(ID_ONE);

        assertEquals(responseDto, actual);
    }

    @Test
    void findById_ShouldThrowException() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(ID_ONE));
    }

    @Test
    void updateEntity_ShouldCallSaveMethodOfRepositoryAndReturnDtoResponse() throws EntityNotFoundException, AccessDeniedException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(repository.findByUsername(USER_NAME)).thenReturn(Optional.of(entity));
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(entity.getId()).thenReturn(ID_ONE);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        DetailedProfileDtoResponse actual = service.updateEntity(requestDto, ID_ONE);

        verify(repository, times(1)).save(entity);
        assertEquals(responseDto, actual);
    }

    @Test
    void updateEntity_ShouldThrowEntityNotFoundException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(repository.findByUsername(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateEntity(requestDto, ID_ONE));
    }

    @Test
    void updateEntity_ShouldThrowAccessDeniedException() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(repository.findByUsername(USER_NAME)).thenReturn(Optional.of(entity));
        when(entity.getId()).thenReturn(ID_ONE);

        assertThrows(AccessDeniedException.class, () -> service.updateEntity(requestDto, ID_TWO));
    }

    @Test
    void deleteEntity_ShouldCallDeleteMethods() throws EntityNotFoundException {
        service.deleteEntity(ID_ONE);

        verify(chatProfileRepository, times(1)).deleteByProfileId(ID_ONE);
        verify(groupParticipantRepository, times(1)).deleteByProfileId(ID_ONE);
        verify(repository, times(1)).deleteById(ID_ONE);
    }

    @Test
    void findAll_ShouldCallFindAllMethodOfRepository() {
        List<Profile> entityList = List.of(entity);
        List<DetailedProfileDtoResponse> responseDtos = List.of(responseDto);
        when(repository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(new PageImpl<>(entityList));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<DetailedProfileDtoResponse> actual = service.findAll(PAGE_SIZE, PAGE_NUMBER);

        verify(repository, times(1)).findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(responseDtos, actual);
    }

    @Test
    void getProfileByUsername_ShouldReturnProfile() throws EntityNotFoundException {
        when(repository.findByUsername(USER_NAME)).thenReturn(Optional.of(entity));

        Profile actual = service.getProfileByUsername(USER_NAME);

        assertEquals(entity, actual);
    }

    @Test
    void getProfileByUsername_ShouldThrowEntityNotFoundException() {
        when(repository.findByUsername(USER_NAME)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getProfileByUsername(USER_NAME));
    }
}