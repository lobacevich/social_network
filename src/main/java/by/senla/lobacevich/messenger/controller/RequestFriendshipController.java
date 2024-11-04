package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.RequestFriendshipDtoRequest;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.enums.RequestStatus;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.RequestFriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend-requests")
@AllArgsConstructor
public class RequestFriendshipController {

    private final RequestFriendshipService service;

    @Operation(summary = "Get all friendship requests",
            description = "Pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<RequestFriendshipDtoResponse> findAll(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @Operation(summary = "Get friendship request by id")
    @GetMapping("/{id}")
    public RequestFriendshipDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Create friendship request")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RequestFriendshipDtoResponse createEntity(@Valid @RequestBody RequestFriendshipDtoRequest dtoRequest) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return service.createEntity(dtoRequest);
    }

    @Operation(summary = "Update friendship request")
    @PreAuthorize("hasRole('ADMIN') or @requestFriendshipServiceImpl.isOwnerOrEmpty(#id)")
    @PutMapping("/{id}")
    public RequestFriendshipDtoResponse updateEntity(@Valid @RequestBody RequestFriendshipDtoRequest dtoRequest,
                                                     @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Delete friendship request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @requestFriendshipServiceImpl.isOwnerOrEmpty(#id)")
    @DeleteMapping("/{id}")
    public void deleteEntity(@PathVariable("id") Long id) throws EntityNotFoundException {
        service.deleteEntity(id);
    }

    @Operation(summary = "Get friend requests by id and status")
    @GetMapping("/search")
    public List<RequestFriendshipDtoResponse> searchRequests(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "profile_id", defaultValue = "0", required = false) Long profileId,
            @RequestParam(value = "status", defaultValue = "APPROVED", required = false) RequestStatus status) throws EntityNotFoundException {
        return service.searchRequests(pageSize, pageNumber, profileId, status);
    }
}
