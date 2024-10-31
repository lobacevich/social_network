package by.senla.lobacevich.messenger.service.impl;

import by.senla.lobacevich.messenger.dto.request.RequestFriendshipDtoRequest;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.entity.RequestFriendship;
import by.senla.lobacevich.messenger.entity.enums.RequestStatus;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.mapper.RequestFriendshipMapper;
import by.senla.lobacevich.messenger.repository.RequestFriendshipRepository;
import by.senla.lobacevich.messenger.service.ProfileService;
import by.senla.lobacevich.messenger.service.RequestFriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class RequestFriendshipServiceImpl extends AbstractService<RequestFriendshipDtoRequest,
        RequestFriendshipDtoResponse, RequestFriendship, RequestFriendshipRepository, RequestFriendshipMapper>
        implements RequestFriendshipService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    public RequestFriendshipServiceImpl(RequestFriendshipRepository dao, RequestFriendshipMapper mapper) {
        super(dao, mapper);
    }

    @Override
    public RequestFriendshipDtoResponse createEntity(RequestFriendshipDtoRequest request, Principal principal) throws InvalidDataException, EntityNotFoundException {
        Profile profile = profileService.getProfileByPrincipal(principal);
        validateRequestDto(request, profile);
        RequestFriendship requestFriendship = new RequestFriendship();
        requestFriendship.setSender(profile);
        requestFriendship.setRecipient(profileService.findEntityById(request.recipientId()));
        requestFriendship.setStatus(RequestStatus.SENT);
        requestFriendship.setUpdateDate(LocalDateTime.now());
        return mapper.entityToDto(repository.save(requestFriendship));
    }

    @Override
    public RequestFriendshipDtoResponse updateEntity(RequestFriendshipDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        RequestFriendship requestFriendship = findEntityById(id);
        requestFriendship.setStatus(request.status() != null ? request.status() : requestFriendship.getStatus());
        return mapper.entityToDto(repository.save(requestFriendship));
    }

    private void validateRequestDto(RequestFriendshipDtoRequest request, Profile profile) throws InvalidDataException {
        if (profile.getId().equals(request.recipientId())) {
            throw new InvalidDataException("Request sent to himself");
        }
        if (repository.findBySenderIdAndRecipientId(profile.getId(), request.recipientId()).isPresent()
                || repository.findBySenderIdAndRecipientId(request.recipientId(), profile.getId()).isPresent()) {
            throw new InvalidDataException("Duplicate friendship request");
        }
    }
}
