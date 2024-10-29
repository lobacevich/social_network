package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;

public interface GroupService extends GenericService<GroupDtoRequest, DetailedGroupDtoResponse, Group> {
}
