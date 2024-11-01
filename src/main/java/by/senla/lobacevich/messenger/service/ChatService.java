package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.util.List;

public interface ChatService extends GenericService<ChatDtoRequest, DetailedChatDtoResponse, Chat> {

    DetailedChatDtoResponse joinChat(Long id) throws EntityNotFoundException;

    DetailedChatDtoResponse leaveChat(Long id) throws EntityNotFoundException;

    List<DetailedChatDtoResponse> searchChats(int pageSize, int pageNumber, String name,
                                              Long ownerId, Long memberId);
}
