package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.User;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.mapper.ProfileMapper;
import by.senla.lobacevich.messenger.repository.ChatProfileRepository;
import by.senla.lobacevich.messenger.repository.GroupParticipantRepository;
import by.senla.lobacevich.messenger.repository.ProfileRepository;
import by.senla.lobacevich.messenger.security.SecurityUtils;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl extends AbstractService<ProfileDtoRequest, DetailedProfileDtoResponse, Profile,
        ProfileRepository, ProfileMapper> implements ProfileService {

    private final ChatProfileRepository chatProfileRepository;
    private final GroupParticipantRepository groupParticipantRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository repository, ProfileMapper mapper, ChatProfileRepository chatProfileRepository, GroupParticipantRepository groupParticipantRepository) {
        super(repository, mapper);
        this.chatProfileRepository = chatProfileRepository;
        this.groupParticipantRepository = groupParticipantRepository;
    }

    @Override
    public DetailedProfileDtoResponse updateEntity(ProfileDtoRequest requestDto, Long id) throws EntityNotFoundException, AccessDeniedException {
        validateProfileOwner(id);
        Profile profile = findEntityById(id);
        profile.setFirstname(requestDto.firstname() != null ? requestDto.firstname() : profile.getFirstname());
        profile.setLastname(requestDto.lastname() != null ? requestDto.lastname() : profile.getLastname());
        return mapper.entityToDto(repository.save(profile));
    }

    private void validateProfileOwner(Long id) throws EntityNotFoundException, AccessDeniedException {
        if (!Objects.equals(getProfileByUsername(SecurityUtils.getAuthenticatedUsername()).getId(), id)) {
            throw new AccessDeniedException("Only owner of profile can update it");
        }
    }

    @Override
    public List<DetailedProfileDtoResponse> searchProfiles(String query, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.searchProfiles(query, pageable).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Profile getProfileByUsername(String username) throws EntityNotFoundException {
        return repository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("Profile not found by username " + username));
    }

    @Override
    public DetailedProfileDtoResponse createEntity(User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(profile));
    }

    @Override
    public void deleteEntity(Long id) throws EntityNotFoundException {
        chatProfileRepository.deleteByProfileId(id);
        groupParticipantRepository.deleteByProfileId(id);
        super.deleteEntity(id);
    }
}
