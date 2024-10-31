package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.RequestFriendshipDtoRequest;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.RequestFriendship;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;

import java.security.Principal;

public interface RequestFriendshipService extends GenericService<RequestFriendshipDtoRequest,
        RequestFriendshipDtoResponse, RequestFriendship>{
    RequestFriendshipDtoResponse createEntity(RequestFriendshipDtoRequest request, Principal principal) throws InvalidDataException, EntityNotFoundException;
}
