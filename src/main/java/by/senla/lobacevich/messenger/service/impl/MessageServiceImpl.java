package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.MessageDtoRequest;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;
import by.senla.lobacevich.messenger.entity.Message;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.MessageMapper;
import by.senla.lobacevich.messenger.repository.MessageRepository;
import by.senla.lobacevich.messenger.security.SecurityUtils;
import by.senla.lobacevich.messenger.service.ChatService;
import by.senla.lobacevich.messenger.service.MessageService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageServiceImpl extends AbstractService<MessageDtoRequest, MessageDtoResponse, Message,
        MessageRepository, MessageMapper> implements MessageService {

    private final ProfileService profileService;
    private final ChatService chatService;

    @Autowired
    public MessageServiceImpl(MessageRepository repository, MessageMapper mapper, ProfileService profileService, ChatService chatService) {
        super(repository, mapper);
        this.profileService = profileService;
        this.chatService = chatService;
    }

    @Override
    public MessageDtoResponse createEntity(MessageDtoRequest request) throws EntityNotFoundException, AccessDeniedException {
        Profile profile = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        validateIsInChat(request, profile);
        Message message = mapper.dtoToEntity(request);
        message.setAuthor(profile);
        message.setChat(chatService.findEntityById(request.chatId()));
        message.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(message));
    }

    private void validateIsInChat(MessageDtoRequest request, Profile profile) throws EntityNotFoundException, AccessDeniedException {
        Chat chat = chatService.findEntityById(request.chatId());
        if (!chat.getParticipants().contains(profile)) {
            throw new AccessDeniedException("Profile id " + profile.getId() + " is not in chat id " + chat.getId());
        }
    }

    @Override
    public MessageDtoResponse updateEntity(MessageDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Message message = findEntityById(id);
        message.setMessage(request.message());
        return mapper.entityToDto(repository.save(message));
    }

    public boolean isOwnerOrEmpty(Long id) {
        return repository.findById(id)
                .map(message -> message.getAuthor().getUser().getUsername().equals(SecurityUtils.getAuthenticatedUsername()))
                .orElse(true);
    }
}
