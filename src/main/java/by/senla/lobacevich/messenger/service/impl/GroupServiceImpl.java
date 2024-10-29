package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.GroupMapper;
import by.senla.lobacevich.messenger.repository.GroupRepository;
import by.senla.lobacevich.messenger.service.GroupService;
import by.senla.lobacevich.messenger.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public DetailedGroupDtoResponse createEntity(GroupDtoRequest request) throws EntityNotFoundException {
        Group group = mapper.dtoToEntity(request);
        group.setOwner(profileService.findEntityById(request.ownerId()));
        group.setCreatedDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(group));
    }

    @Override
    public DetailedGroupDtoResponse updateEntity(GroupDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        Group group = findEntityById(id);
        group.setName(request.name() != null ? request.name() : group.getName());
        group.setOwner(request.ownerId() != null ? profileService.findEntityById(request.ownerId()) :
                group.getOwner());
        return mapper.entityToDto(repository.save(group));
    }
}
