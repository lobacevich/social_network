package by.senla.lobacevich.messenger.mapper;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.entity.Group;
import org.mapstruct.Mapper;

@Mapper
public interface GroupMapper extends GenericMapper<GroupDtoRequest,
        DetailedGroupDtoResponse, Group> {
}
