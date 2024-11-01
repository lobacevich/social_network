package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.RequestFriendshipDtoRequest;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.RequestFriendship;
import by.senla.lobacevich.messenger.entity.enums.RequestStatus;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.util.List;

public interface RequestFriendshipService extends GenericService<RequestFriendshipDtoRequest,
        RequestFriendshipDtoResponse, RequestFriendship>{

    List<RequestFriendshipDtoResponse> getFriends(int pageSize, int pageNumber, Long profileId, RequestStatus status) throws EntityNotFoundException;
}
