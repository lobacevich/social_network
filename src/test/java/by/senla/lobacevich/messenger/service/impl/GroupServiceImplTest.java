package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.GroupMapper;
import by.senla.lobacevich.messenger.repository.GroupRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    private static final Long ID_ONE = 1L;
    private static final Long ID_ZERO = 0L;
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 0;
    private static final String USER_NAME = "UserName";
    private static final String OTHER_USER_NAME = "OtherUserName";
    private static final String GROUP_NAME = "GroupName";

    @Mock
    private GroupRepository repository;

    @Mock
    private GroupMapper mapper;

    @Mock
    private GroupDtoRequest requestDto;

    @Mock
    private DetailedGroupDtoResponse responseDto;

    @Mock
    private Group entity;

    @Mock
    private ProfileService profileService;

    @Mock
    private Profile profile;

    @Mock
    private User user;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private GroupServiceImpl service;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createEntity_ShouldCallSaveMethodOfRepositoryAndReturnDtoResponse() throws EntityNotFoundException {
        when(mapper.dtoToEntity(requestDto)).thenReturn(entity);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        DetailedGroupDtoResponse actual = service.createEntity(requestDto);

        verify(repository, times(1)).save(entity);
        assertEquals(responseDto, actual);
    }

    @Test
    void findById_ShouldReturnDtoResponse() throws EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        DetailedGroupDtoResponse actual = service.findById(ID_ONE);

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

        DetailedGroupDtoResponse actual = service.updateEntity(requestDto, ID_ONE);

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
        service.deleteEntity(ID_ONE);

        verify(repository, times(1)).deleteById(ID_ONE);
    }

    @Test
    void findAll_ShouldCallFindAllMethodOfRepository() {
        List<Group> entityList = List.of(entity);
        List<DetailedGroupDtoResponse> responseDtos = List.of(responseDto);
        when(repository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(new PageImpl<>(entityList));
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<DetailedGroupDtoResponse> actual = service.findAll(PAGE_SIZE, PAGE_NUMBER);

        verify(repository, times(1)).findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(responseDtos, actual);
    }

    @Test
    void isOwnerOrEmpty_ShouldReturnIsOwnerTrue() {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(entity.getOwner()).thenReturn(profile);
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
        when(entity.getOwner()).thenReturn(profile);
        when(profile.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn(USER_NAME);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(OTHER_USER_NAME);

        assertFalse(service.isOwnerOrEmpty(ID_ONE));
    }

    @Test
    void joinChat_ShouldAddParticipantAndSaveGroup() throws EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        DetailedGroupDtoResponse actual = service.joinGroup(ID_ONE);

        verify(repository, times(1)).save(entity);
        assertEquals(responseDto, actual);
    }

    @Test
    void leaveChat_ShouldRemoveParticipantAndSaveGroup() throws EntityNotFoundException {
        when(repository.findById(ID_ONE)).thenReturn(Optional.of(entity));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_NAME);
        when(profileService.getProfileByUsername(USER_NAME)).thenReturn(profile);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        DetailedGroupDtoResponse actual = service.leaveGroup(ID_ONE);

        verify(repository, times(1)).save(entity);
        assertEquals(responseDto, actual);
    }

    @Test
    void searchChats_ShouldReturnGroupsByOwnerId() {
        Page<Group> groupPage = new PageImpl<>(List.of(entity));
        when(repository.findByOwnerIdAndNameLike(ID_ONE, GROUP_NAME, PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(groupPage);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<DetailedGroupDtoResponse> actual = service.searchGroups(PAGE_SIZE, PAGE_NUMBER, GROUP_NAME, ID_ONE, ID_ZERO);

        verify(repository, times(1)).findByOwnerIdAndNameLike(ID_ONE, GROUP_NAME, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(List.of(responseDto), actual);
    }

    @Test
    void searchChats_ShouldReturnGroupsByMemberId() {
        Page<Group> groupPage = new PageImpl<>(List.of(entity));
        when(repository.findByParticipantIdAndNameLike(ID_ONE, GROUP_NAME, PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(groupPage);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<DetailedGroupDtoResponse> actual = service.searchGroups(PAGE_SIZE, PAGE_NUMBER, GROUP_NAME, ID_ZERO, ID_ONE);

        verify(repository, times(1)).findByParticipantIdAndNameLike(ID_ONE, GROUP_NAME, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(List.of(responseDto), actual);
    }

    @Test
    void searchChats_ShouldReturnGroupsByName() {
        Page<Group> groupPage = new PageImpl<>(List.of(entity));
        when(repository.searchGroups(GROUP_NAME, PageRequest.of(PAGE_NUMBER, PAGE_SIZE))).thenReturn(groupPage);
        when(mapper.entityToDto(entity)).thenReturn(responseDto);

        List<DetailedGroupDtoResponse> actual = service.searchGroups(PAGE_SIZE, PAGE_NUMBER, GROUP_NAME, ID_ZERO, ID_ZERO);

        verify(repository, times(1)).searchGroups(GROUP_NAME, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        assertEquals(List.of(responseDto), actual);
    }
}
