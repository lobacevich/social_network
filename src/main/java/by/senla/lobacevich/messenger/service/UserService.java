package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;

public interface UserService extends GenericService<UserDtoRequest, UserDtoResponse, User> {

    User getUserByUsername(String username) throws EntityNotFoundException;

    UserDtoResponse createEntity(UserDtoRequest request) throws InvalidDataException;

    UserDtoResponse makeAdmin(Long id) throws EntityNotFoundException;

    UserDtoResponse makeUser(Long id) throws EntityNotFoundException;
}
