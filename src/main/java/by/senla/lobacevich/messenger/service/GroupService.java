package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.util.List;

public interface GroupService extends GenericService<GroupDtoRequest, DetailedGroupDtoResponse, Group> {

    DetailedGroupDtoResponse joinGroup(Long id) throws EntityNotFoundException;

    DetailedGroupDtoResponse leaveGroup(Long id) throws EntityNotFoundException;

    List<DetailedGroupDtoResponse> searchGroups(int pageSize, int pageNumber, String name,
                                                Long ownerId, Long memberId);
}
