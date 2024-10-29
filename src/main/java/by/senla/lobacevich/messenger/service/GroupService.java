package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.security.Principal;

public interface GroupService extends GenericService<GroupDtoRequest, DetailedGroupDtoResponse, Group> {

    DetailedGroupDtoResponse joinGroup(Long id, Principal principal) throws EntityNotFoundException;

    DetailedGroupDtoResponse leaveGroup(Long id, Principal principal) throws EntityNotFoundException;
}
