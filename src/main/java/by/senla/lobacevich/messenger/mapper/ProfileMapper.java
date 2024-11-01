package by.senla.lobacevich.messenger.mapper;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import org.mapstruct.Mapper;

@Mapper
public interface ProfileMapper extends GenericMapper<ProfileDtoRequest, DetailedProfileDtoResponse, Profile> {
}
