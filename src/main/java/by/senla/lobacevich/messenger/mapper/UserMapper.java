package by.senla.lobacevich.messenger.mapper;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper extends GenericMapper<UserDtoRequest, UserDtoResponse, User> {
}
