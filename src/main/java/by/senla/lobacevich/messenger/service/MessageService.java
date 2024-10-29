package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.MessageDtoRequest;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.entity.Message;

public interface MessageService extends GenericService<MessageDtoRequest, MessageDtoResponse, Message> {
}
