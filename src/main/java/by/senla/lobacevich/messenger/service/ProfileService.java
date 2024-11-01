package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.util.List;

public interface ProfileService extends GenericService<ProfileDtoRequest, DetailedProfileDtoResponse, Profile> {

    List<DetailedProfileDtoResponse> searchProfiles(String query, int pageSize, int pageNumber);

    Profile getProfileByUsername(String username) throws EntityNotFoundException;
}
