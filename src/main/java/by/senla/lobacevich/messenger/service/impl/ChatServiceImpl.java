package by.senla.lobacevich.messenger.service.impl;


import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.ChatMapper;
import by.senla.lobacevich.messenger.repository.ChatRepository;
import by.senla.lobacevich.messenger.service.ChatService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class ChatServiceImpl extends AbstractService<ChatDtoRequest, DetailedChatDtoResponse, Chat,
        ChatRepository, ChatMapper> implements ChatService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    public ChatServiceImpl(ChatRepository repository, ChatMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public DetailedChatDtoResponse createEntity(ChatDtoRequest requestDto) {
        Chat chat = mapper.dtoToEntity(requestDto);
        chat.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(chat));
    }

    @Override
    public DetailedChatDtoResponse updateEntity(ChatDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Chat chat = findEntityById(id);
        chat.setName(request.name() != null ? request.name() : chat.getName());
        return mapper.entityToDto(repository.save(chat));
    }

    @Override
    public DetailedChatDtoResponse joinChat(Long id, Principal principal) throws EntityNotFoundException {
        Chat chat = findEntityById(id);
        Profile profile = profileService.getProfileByPrincipal(principal);
        chat.getParticipants().add(profile);
        return mapper.entityToDto(repository.save(chat));
    }

    @Override
    public DetailedChatDtoResponse leaveChat(Long id, Principal principal) throws EntityNotFoundException {
        Chat chat = findEntityById(id);
        Profile profile = profileService.getProfileByPrincipal(principal);
        chat.getParticipants().remove(profile);
        return mapper.entityToDto(repository.save(chat));
    }
}
