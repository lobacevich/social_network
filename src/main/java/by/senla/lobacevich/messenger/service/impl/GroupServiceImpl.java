package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.GroupMapper;
import by.senla.lobacevich.messenger.repository.GroupRepository;
import by.senla.lobacevich.messenger.service.GroupService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends AbstractService<GroupDtoRequest, DetailedGroupDtoResponse, Group,
        GroupRepository, GroupMapper> implements GroupService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    public GroupServiceImpl(GroupRepository repository, GroupMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public DetailedGroupDtoResponse createEntity(GroupDtoRequest request, Principal principal) throws EntityNotFoundException {
        Group group = mapper.dtoToEntity(request);
        group.setOwner(profileService.getProfileByPrincipal(principal));
        group.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public DetailedGroupDtoResponse updateEntity(GroupDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Group group = findEntityById(id);
        group.setName(request.name() != null ? request.name() : group.getName());
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public DetailedGroupDtoResponse joinGroup(Long id, Principal principal) throws EntityNotFoundException {
        Group group = findEntityById(id);
        Profile profile = profileService.getProfileByPrincipal(principal);
        group.getParticipants().add(profile);
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public DetailedGroupDtoResponse leaveGroup(Long id, Principal principal) throws EntityNotFoundException {
        Group group = findEntityById(id);
        Profile profile = profileService.getProfileByPrincipal(principal);
        group.getParticipants().remove(profile);
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public List<DetailedGroupDtoResponse> searchGroups(String name, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.searchGroups(name, pageable).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }
}
