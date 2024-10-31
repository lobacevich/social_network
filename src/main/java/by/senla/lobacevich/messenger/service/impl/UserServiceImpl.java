package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.entity.enums.Role;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.UserMapper;
import by.senla.lobacevich.messenger.repository.UserRepository;
import by.senla.lobacevich.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractService<UserDtoRequest, UserDtoResponse, User,
        UserRepository, UserMapper> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public User getUserByUsername(String username) throws EntityNotFoundException {
        return repository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User with username " + username + " not found"));
    }

    @Override
    public UserDtoResponse createEntity(UserDtoRequest request) throws InvalidDataException {
        User user = mapper.dtoToEntity(request);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(request.password()));
        try {
            return mapper.entityToDto(repository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate username or email");
        }
    }

    @Override
    public UserDtoResponse updateEntity(UserDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        User user = findEntityById(id);
        user.setUsername(request.username() != null ? request.username() :
                user.getUsername());
        user.setEmail(request.email() != null ? request.email() :
                user.getEmail());
        user.setPassword(request.password() != null ? passwordEncoder.encode(request.password()) :
                user.getPassword());
        try {
            return mapper.entityToDto(repository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate username or email");
        }
    }

    @Override
    public UserDtoResponse makeAdmin(Long id) throws EntityNotFoundException {
        User user = findEntityById(id);
        user.setRole(Role.ROLE_ADMIN);
        return mapper.entityToDto(repository.save(user));
    }

    @Override
    public UserDtoResponse makeUser(Long id) throws EntityNotFoundException {
        User user = findEntityById(id);
        user.setRole(Role.ROLE_USER);
        return mapper.entityToDto(repository.save(user));
    }
}
