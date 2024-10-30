package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.MessageDtoRequest;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;
import by.senla.lobacevich.messenger.entity.Message;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.MessageMapper;
import by.senla.lobacevich.messenger.repository.MessageRepository;
import by.senla.lobacevich.messenger.service.ChatService;
import by.senla.lobacevich.messenger.service.MessageService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageServiceImpl extends AbstractService<MessageDtoRequest, MessageDtoResponse, Message,
        MessageRepository, MessageMapper> implements MessageService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ChatService chatService;

    @Autowired
    public MessageServiceImpl(MessageRepository repository, MessageMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public MessageDtoResponse createEntity(MessageDtoRequest request) throws EntityNotFoundException, AuthorizationException {
        validateIsInChat(request);
        Message message = mapper.dtoToEntity(request);
        message.setAuthor(profileService.findEntityById(request.authorId()));
        message.setChat(chatService.findEntityById(request.chatId()));
        message.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(message));
    }

    private void validateIsInChat(MessageDtoRequest request) throws EntityNotFoundException, AuthorizationException {
        Chat chat = chatService.findEntityById(request.chatId());
        Profile profile = profileService.findEntityById(request.authorId());
        if (!chat.getParticipants().contains(profile)) {
            throw new AuthorizationException("Profile id " + profile.getId() + " is not in chat id " + chat.getId());
        }
    }

    @Override
    public MessageDtoResponse updateEntity(MessageDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Message message = findEntityById(id);
        message.setMessage(request.message());
        return mapper.entityToDto(repository.save(message));
    }
}
