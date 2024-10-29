package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;

public interface ChatService extends GenericService<ChatDtoRequest, DetailedChatDtoResponse, Chat> {
}
