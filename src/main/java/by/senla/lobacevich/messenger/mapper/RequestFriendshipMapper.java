package by.senla.lobacevich.messenger.mapper;

import by.senla.lobacevich.messenger.dto.request.RequestFriendshipDtoRequest;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.RequestFriendship;
import org.mapstruct.Mapper;

@Mapper
public interface RequestFriendshipMapper extends GenericMapper<RequestFriendshipDtoRequest,
        RequestFriendshipDtoResponse, RequestFriendship> {
}
