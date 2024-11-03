package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.entity.enums.Role;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.ProfileMapper;
import by.senla.lobacevich.messenger.mapper.UserMapper;
import by.senla.lobacevich.messenger.repository.ChatProfileRepository;
import by.senla.lobacevich.messenger.repository.GroupParticipantRepository;
import by.senla.lobacevich.messenger.repository.ProfileRepository;
import by.senla.lobacevich.messenger.repository.UserRepository;
import by.senla.lobacevich.messenger.security.SecurityUtils;
import by.senla.lobacevich.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends AbstractService<UserDtoRequest, UserDtoResponse, User,
        UserRepository, UserMapper> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ChatProfileRepository chatProfileRepository;
    private final GroupParticipantRepository groupParticipantRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder,
                           ProfileRepository profileRepository, ProfileMapper profileMapper, ChatProfileRepository chatProfileRepository, GroupParticipantRepository groupParticipantRepository) {
        super(repository, mapper);
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.chatProfileRepository = chatProfileRepository;
        this.groupParticipantRepository = groupParticipantRepository;
    }

    @Transactional
    @Override
    public DetailedProfileDtoResponse createUserAndProfile(UserDtoRequest request) throws InvalidDataException {
        User user = mapper.dtoToEntity(request);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(request.password()));
        try {
            repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate username or email");
        }
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setCreatedDate(LocalDateTime.now());
        return profileMapper.entityToDto(profileRepository.save(profile));
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

    @Transactional
    @Override
    public void deleteUserAndProfile(Long id) throws EntityNotFoundException {
        Profile profile = profileRepository.findByUsername(findEntityById(id).getUsername()).orElseThrow(() ->
                new EntityNotFoundException("Profile with id " + id + " not found"));
        chatProfileRepository.deleteByProfileId(profile.getId());
        groupParticipantRepository.deleteByProfileId(profile.getId());
        profileRepository.deleteById(profile.getId());
        super.deleteUserAndProfile(id);
    }

    public boolean isOwnerOrEmpty(Long id) {
        return repository.findById(id)
                .map(user -> user.getUsername().equals(SecurityUtils.getAuthenticatedUsername()))
                .orElse(true);
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
