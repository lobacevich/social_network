package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.GroupDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService service;

    @Operation(summary = "Get group by id")
    @GetMapping("/{id}")
    public DetailedGroupDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Create group")
    @PostMapping
    public ResponseEntity<DetailedGroupDtoResponse> createEntity(@Valid @RequestBody GroupDtoRequest dtoRequest) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return new ResponseEntity<>(service.createEntity(dtoRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update group")
    @PreAuthorize("hasRole('ADMIN') or @groupServiceImpl.isOwnerOrEmpty(#id)")
    @PutMapping("/{id}")
    public DetailedGroupDtoResponse updateEntity(@Valid @RequestBody GroupDtoRequest dtoRequest, @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Delete group")
    @PreAuthorize("hasRole('ADMIN') or @groupServiceImpl.isOwnerOrEmpty(#id)")
    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id) throws EntityNotFoundException {
        service.deleteUserAndProfile(id);
        return HttpStatus.NO_CONTENT;
    }

    @Operation(summary = "Add profile to group by id")
    @PostMapping("{id}/members")
    public DetailedGroupDtoResponse joinGroup(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.joinGroup(id);

    }

    @Operation(summary = "Remove profile from group by id")
    @DeleteMapping("{id}/members")
    public DetailedGroupDtoResponse leaveGroup(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.leaveGroup(id);
    }

    @Operation(summary = "Search group by name, owner_id and member_id",
            description = "Case independent search group by part of group name, owner_id and member_id, " +
                    "pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<DetailedGroupDtoResponse> searchGroups(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "owner_id", defaultValue = "0", required = false) Long ownerId,
            @RequestParam(value = "member_id", defaultValue = "0", required = false) Long memberId) {
        return service.searchGroups(pageSize, pageNumber, name, ownerId, memberId);
    }
}
