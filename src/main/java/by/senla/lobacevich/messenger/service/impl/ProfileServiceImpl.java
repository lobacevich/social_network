package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.ProfileMapper;
import by.senla.lobacevich.messenger.repository.ProfileRepository;
import by.senla.lobacevich.messenger.service.ProfileService;
import by.senla.lobacevich.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProfileServiceImpl extends AbstractService<ProfileDtoRequest, DetailedProfileDtoResponse, Profile,
        ProfileRepository, ProfileMapper> implements ProfileService {

    @Autowired
    private UserService userService;

    public ProfileServiceImpl(ProfileRepository repository, ProfileMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public DetailedProfileDtoResponse createEntity(ProfileDtoRequest request) throws InvalidDataException, EntityNotFoundException {
        Profile profile = mapper.dtoToEntity(request);
        profile.setUser(userService.findEntityById(request.userId()));
        profile.setCreatedDate(LocalDateTime.now());
        try {
            return mapper.entityToDto(repository.save(profile));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("User id " + request.userId() + " already has profile");
        }
    }

    @Override
    public DetailedProfileDtoResponse updateEntity(ProfileDtoRequest requestDto, Long id) throws EntityNotFoundException {
        Profile profile = findEntityById(id);
        profile.setFirstname(requestDto.firstname() != null ? requestDto.firstname() : profile.getFirstname());
        profile.setLastname(requestDto.lastname() != null ? requestDto.lastname() : profile.getLastname());
        return mapper.entityToDto(repository.save(profile));
    }
}
