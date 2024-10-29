package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.RequestFriendshipDtoRequest;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.RequestFriendship;

public interface RequestFriendshipService extends GenericService<RequestFriendshipDtoRequest,
        RequestFriendshipDtoResponse, RequestFriendship>{
}
