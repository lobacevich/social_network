package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;

public interface ProfileService extends GenericService<ProfileDtoRequest, DetailedProfileDtoResponse, Profile> {
}
