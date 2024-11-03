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
import by.senla.lobacevich.messenger.security.SecurityUtils;
import by.senla.lobacevich.messenger.service.ProfileService;
import by.senla.lobacevich.messenger.service.RequestFriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestFriendshipServiceImpl extends AbstractService<RequestFriendshipDtoRequest,
        RequestFriendshipDtoResponse, RequestFriendship, RequestFriendshipRepository, RequestFriendshipMapper>
        implements RequestFriendshipService {

    private final ProfileService profileService;

    @Autowired
    public RequestFriendshipServiceImpl(RequestFriendshipRepository dao, RequestFriendshipMapper mapper, ProfileService profileService) {
        super(dao, mapper);
        this.profileService = profileService;
    }

    @Override
    public RequestFriendshipDtoResponse createEntity(RequestFriendshipDtoRequest request) throws EntityNotFoundException, InvalidDataException {
        Profile profile = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername());
        validateRequestDto(request, profile);
        RequestFriendship requestFriendship = new RequestFriendship();
        requestFriendship.setSender(profile);
        requestFriendship.setRecipient(profileService.findEntityById(request.recipientId()));
        requestFriendship.setStatus(RequestStatus.SENT);
        requestFriendship.setUpdateDate(LocalDateTime.now());
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

    @Override
    public RequestFriendshipDtoResponse updateEntity(RequestFriendshipDtoRequest request, Long id) throws EntityNotFoundException, InvalidDataException {
        RequestFriendship requestFriendship = findEntityById(id);
        requestFriendship.setStatus(request.status() != null ? request.status() : requestFriendship.getStatus());
        return mapper.entityToDto(repository.save(requestFriendship));
    }

    public boolean isOwnerOrEmpty(Long id) {
        return repository.findById(id)
                .map(requestFriendship ->
                        requestFriendship.getSender().getUser().getUsername().equals(SecurityUtils.getAuthenticatedUsername()) ||
                        requestFriendship.getRecipient().getUser().getUsername().equals(SecurityUtils.getAuthenticatedUsername())
                )
                .orElse(true);
    }

    @Override
    public List<RequestFriendshipDtoResponse> searchRequests(int pageSize, int pageNumber, Long profileId, RequestStatus status) throws EntityNotFoundException {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<RequestFriendship> result;

        if (profileId == 0) {
            Long id = profileService.getProfileByUsername(SecurityUtils.getAuthenticatedUsername()).getId();
            result = repository.findByStatusAndSenderIdOrStatusAndRecipientId(status, id, status, id, pageable);
        } else {
            result = repository.findByStatusAndSenderIdOrStatusAndRecipientId(status, profileId, status, profileId, pageable);
        }

        return result.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }
}
