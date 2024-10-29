package by.senla.lobacevich.messenger.mapper;

import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.entity.Chat;
import org.mapstruct.Mapper;

@Mapper
public interface ChatMapper extends GenericMapper<ChatDtoRequest, DetailedChatDtoResponse, Chat> {
}
