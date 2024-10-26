package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

public interface UserService {

    UserDtoResponse createEntity(UserDtoRequest request);

    User getUserByUsername(String username) throws EntityNotFoundException;
}
