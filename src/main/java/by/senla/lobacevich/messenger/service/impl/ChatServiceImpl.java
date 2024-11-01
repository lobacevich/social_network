package by.senla.lobacevich.messenger.service.impl;


import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.ChatMapper;
import by.senla.lobacevich.messenger.repository.ChatRepository;
import by.senla.lobacevich.messenger.security.SecurityUtils;
import by.senla.lobacevich.messenger.service.ChatService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl extends AbstractService<ChatDtoRequest, DetailedChatDtoResponse, Chat,
        ChatRepository, ChatMapper> implements ChatService {

    private final ProfileService profileService;

    @Autowired
    public ChatServiceImpl(ChatRepository repository, ChatMapper mapper, ProfileService profileService) {
        super(repository, mapper);
        this.profileService = profileService;
    }

    @Override
    public DetailedChatDtoResponse createEntity(ChatDtoRequest request) throws EntityNotFoundException {
        Chat chat = mapper.dtoToEntity(request);
        chat.setCreatedDate(LocalDateTime.now());
        Profile owner = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        chat.setOwner(owner);
        chat.getParticipants().addAll(List.of(owner, profileService.findEntityById(request.partnerId())));
        return mapper.entityToDto(repository.save(chat));
    }

    @Override
    public DetailedChatDtoResponse updateEntity(ChatDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Chat chat = findEntityById(id);
        chat.setName(request.name() != null ? request.name() : chat.getName());
        return mapper.entityToDto(repository.save(chat));
    }

    public boolean isOwnerOrEmpty(Long id) {
        return repository.findById(id)
                .map(chat -> chat.getOwner().getUser().getUsername().equals(SecurityUtils.getAuthenticatedUsername()))
                .orElse(true);
    }

    @Override
    public DetailedChatDtoResponse joinChat(Long id) throws EntityNotFoundException {
        Chat chat = findEntityById(id);
        Profile profile = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        chat.getParticipants().add(profile);
        return mapper.entityToDto(repository.save(chat));
    }

    @Override
    public DetailedChatDtoResponse leaveChat(Long id) throws EntityNotFoundException {
        Chat chat = findEntityById(id);
        Profile profile = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        chat.getParticipants().remove(profile);
        return mapper.entityToDto(repository.save(chat));
    }

    @Override
    public List<DetailedChatDtoResponse> searchChats(int pageSize, int pageNumber, String name,
                                                     Long ownerId, Long memberId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Chat> result;

        if (ownerId != 0) {
            result = repository.findByOwnerIdAndNameLike(ownerId, name, pageable);
        } else if (memberId != 0) {
            result = repository.findByParticipantIdAndNameLike(memberId, name, pageable);
        } else {
            result = repository.searchChats(name, pageable);
        }

        return result.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }
}
