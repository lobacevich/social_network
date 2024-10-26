package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.entity.enums.Role;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.mapper.UserMapper;
import by.senla.lobacevich.messenger.repository.UserRepository;
import by.senla.lobacevich.messenger.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDtoResponse createEntity(UserDtoRequest request) {
        User user = mapper.dtoToEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        return mapper.entityToDto(repository.save(user));
    }

    @Override
    public User getUserByUsername(String username) throws EntityNotFoundException {
        return repository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User with username " + username + " not found"));
    }
}
