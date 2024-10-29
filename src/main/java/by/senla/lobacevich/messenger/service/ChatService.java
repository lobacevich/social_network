package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.security.Principal;

public interface ChatService extends GenericService<ChatDtoRequest, DetailedChatDtoResponse, Chat> {

    DetailedChatDtoResponse joinChat(Long id, Principal principal) throws EntityNotFoundException;

    DetailedChatDtoResponse leaveChat(Long id, Principal principal) throws EntityNotFoundException;
}
