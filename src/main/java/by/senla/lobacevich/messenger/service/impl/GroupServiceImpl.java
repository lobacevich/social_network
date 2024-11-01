package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.GroupMapper;
import by.senla.lobacevich.messenger.repository.GroupRepository;
import by.senla.lobacevich.messenger.security.SecurityUtils;
import by.senla.lobacevich.messenger.service.GroupService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends AbstractService<GroupDtoRequest, DetailedGroupDtoResponse, Group,
        GroupRepository, GroupMapper> implements GroupService {

    private final ProfileService profileService;

    @Autowired
    public GroupServiceImpl(GroupRepository repository, GroupMapper mapper, ProfileService profileService) {
        super(repository, mapper);
        this.profileService = profileService;
    }

    @Override
    public DetailedGroupDtoResponse createEntity(GroupDtoRequest request) throws EntityNotFoundException {
        Group group = mapper.dtoToEntity(request);
        Profile owner = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        group.setOwner(owner);
        group.setCreatedDate(LocalDateTime.now());
        group.getParticipants().add(owner);
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public DetailedGroupDtoResponse updateEntity(GroupDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Group group = findEntityById(id);
        group.setName(request.name() != null ? request.name() : group.getName());
        return mapper.entityToDto(repository.save(group));
    }

    public boolean isOwnerOrEmpty(Long id) {
        return repository.findById(id)
                .map(group -> group.getOwner().getUser().getUsername().equals(SecurityUtils.getAuthenticatedUsername()))
                .orElse(true);
    }

    @Override
    public DetailedGroupDtoResponse joinGroup(Long id) throws EntityNotFoundException {
        Group group = findEntityById(id);
        Profile profile = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        group.getParticipants().add(profile);
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public DetailedGroupDtoResponse leaveGroup(Long id) throws EntityNotFoundException {
        Group group = findEntityById(id);
        Profile profile = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        group.getParticipants().remove(profile);
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public List<DetailedGroupDtoResponse> searchGroups(int pageSize, int pageNumber, String name,
                                                       Long ownerId, Long memberId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Group> result;

        if (ownerId != 0) {
            result = repository.findByOwnerIdAndNameLike(ownerId, name, pageable);
        } else if (memberId != 0) {
            result = repository.findByParticipantIdAndNameLike(memberId, name, pageable);
        } else {
            result = repository.searchGroups(name, pageable);
        }

        return result.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }
}
