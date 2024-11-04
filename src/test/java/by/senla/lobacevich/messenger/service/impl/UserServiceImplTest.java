package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.UserMapper;
import by.senla.lobacevich.messenger.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final Long ID_ONE = 1L;
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 0;
    private static final String USER_NAME = "UserName";
    private static final String OTHER_USER_NAME = "OtherUserName";

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private UserDtoRequest requestDto;

    @Mock
    private UserDtoResponse responseDto;

    @Mock
    private User entity;

    @Mock
    private ProfileService profileService;

    @Mock
    private Profile profile;

    @Mock
    private DetailedProfileDtoResponse profileDtoResponse;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createUserAndProfile_ShouldCallSaveMethodOfUserRepositoryAndProfileServiceAndReturnProfileDtoResponse() throws InvalidDataException {
        when(mapper.dtoToEntity(requestDto)).thenReturn(entity);
        when(profileService.createEntity(entity)).thenReturn(profileDtoResponse);

        DetailedProfileDtoResponse actual = service.createUserAndProfile(requestDto);

        verify(repository, times(1)).save(entity);
        verify(profileService, times(1)).createEntity(entity);
        assertEquals(profileDtoResponse, actual);
    }

    @Test
    void findById_ShouldReturnDtoResponse() throws EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        UserDtoResponse actual = service.findById(ID_ONE);

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

        UserDtoResponse actual = service.updateEntity(requestDto, ID_ONE);

        verify(repository, times(1)).save(entity);
        assertEquals(responseDto, actual);
    }

    @Test
    void updateEntity_ShouldThrowException() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateEntity(requestDto, ID_ONE));
    }

    @Test
    void deleteUserAndProfile_ShouldCallDeleteByIdMethodAndDeleteByIdMethodOfProfileService() throws EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(entity.getUsername()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(profile.getId()).thenReturn(ID_ONE);

        service.deleteEntity(ID_ONE);

        verify(profileService, times(1)).deleteEntity(ID_ONE);
        verify(repository, times(1)).deleteById(ID_ONE);
    }

    @Test
    void findAll_ShouldCallFindAllMethodOfRepository() {
        List<User> entityList = List.of(entity);
        List<UserDtoResponse> responseDtos = List.of(responseDto);
        when(repository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(new PageImpl<>(entityList));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<UserDtoResponse> actual = service.findAll(PAGE_SIZE, PAGE_NUMBER);

        verify(repository, times(1)).findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(responseDtos, actual);
    }

    @Test
    void isOwnerOrEmpty_ShouldReturnIsOwnerTrue() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(entity.getUsername()).thenReturn(USER_NAME);

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
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(entity.getUsername()).thenReturn(OTHER_USER_NAME);

        assertFalse(service.isOwnerOrEmpty(ID_ONE));
    }
}
