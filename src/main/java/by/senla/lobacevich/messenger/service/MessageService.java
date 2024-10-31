package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.MessageDtoRequest;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.entity.Message;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.security.Principal;

public interface MessageService extends GenericService<MessageDtoRequest, MessageDtoResponse, Message> {
    MessageDtoResponse createEntity(MessageDtoRequest request, Principal principal) throws EntityNotFoundException, AuthorizationException;
}
