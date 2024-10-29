package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.RequestFriendship;
import by.senla.lobacevich.messenger.entity.enums.RequestStatus;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.ChatMapper;
import by.senla.lobacevich.messenger.mapper.CommentMapper;
import by.senla.lobacevich.messenger.mapper.GroupMapper;
import by.senla.lobacevich.messenger.mapper.MessageMapper;
import by.senla.lobacevich.messenger.mapper.PostMapper;
import by.senla.lobacevich.messenger.mapper.ProfileMapper;
import by.senla.lobacevich.messenger.mapper.RequestFriendshipMapper;
import by.senla.lobacevich.messenger.repository.ProfileRepository;
import by.senla.lobacevich.messenger.service.ProfileService;
import by.senla.lobacevich.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl extends AbstractService<ProfileDtoRequest, DetailedProfileDtoResponse, Profile,
        ProfileRepository, ProfileMapper> implements ProfileService {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RequestFriendshipMapper requestFriendshipMapper;

    @Autowired
    public ProfileServiceImpl(ProfileRepository repository, ProfileMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public DetailedProfileDtoResponse createEntity(ProfileDtoRequest request) throws InvalidDataException, EntityNotFoundException {
        Profile profile = mapper.dtoToEntity(request);
        profile.setUser(userService.findEntityById(request.userId()));
        profile.setCreatedDate(LocalDateTime.now());
        try {
            return mapper.entityToDto(repository.save(profile));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("User id " + request.userId() + " already has profile");
        }
    }

    @Override
    public DetailedProfileDtoResponse updateEntity(ProfileDtoRequest requestDto, Long id) throws EntityNotFoundException {
        Profile profile = findEntityById(id);
        profile.setFirstname(requestDto.firstname() != null ? requestDto.firstname() : profile.getFirstname());
        profile.setLastname(requestDto.lastname() != null ? requestDto.lastname() : profile.getLastname());
        return mapper.entityToDto(repository.save(profile));
    }

    @Override
    public Profile getProfileByPrincipal(Principal principal) throws EntityNotFoundException {
        return repository.findByUsername(principal.getName()).orElseThrow(() ->
                new EntityNotFoundException("Profile not found by username " + principal.getName()));
    }

    @Override
    public List<DetailedGroupDtoResponse> getProfileGroups(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getGroups().stream()
                .map(groupMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailedGroupDtoResponse> getProfileGroupsOwned(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getOwnedGroups().stream()
                .map(groupMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailedChatDtoResponse> getProfileChats(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getChats().stream()
                .map(chatMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailedPostDtoResponse> getProfilePosts(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getPosts().stream()
                .map(postMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDtoResponse> getProfileMessages(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getMessages().stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDtoResponse> getProfileComments(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getComments().stream()
                .map(commentMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailedProfileDtoResponse> getProfileFriends(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        List<DetailedProfileDtoResponse> friends = profile.getSendFriendRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED)
                .map(RequestFriendship::getRecipient)
                .map(mapper::entityToDto)
                .collect(Collectors.toList());

        friends.addAll(profile.getReceiveFriendRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.APPROVED)
                .map(RequestFriendship::getSender)
                .map(mapper::entityToDto)
                .toList());

        return friends;
    }

    @Override
    public List<RequestFriendshipDtoResponse> getProfileReceivedFriendRequests(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getReceiveFriendRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.SENT)
                .map(requestFriendshipMapper::entityToDto)
                .toList();
    }

    @Override
    public List<RequestFriendshipDtoResponse> getProfileSendFriendRequests(Principal principal) throws EntityNotFoundException {
        Profile profile = getProfileByPrincipal(principal);
        return profile.getSendFriendRequests().stream()
                .filter(request -> request.getStatus() == RequestStatus.SENT)
                .map(requestFriendshipMapper::entityToDto)
                .toList();
    }
}
