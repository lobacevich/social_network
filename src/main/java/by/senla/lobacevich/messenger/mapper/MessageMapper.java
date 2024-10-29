package by.senla.lobacevich.messenger.mapper;

import by.senla.lobacevich.messenger.dto.request.MessageDtoRequest;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.entity.Message;
import org.mapstruct.Mapper;

@Mapper
public interface MessageMapper extends GenericMapper<MessageDtoRequest, MessageDtoResponse, Message> {
}
