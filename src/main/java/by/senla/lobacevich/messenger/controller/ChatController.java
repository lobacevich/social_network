package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
@RequestMapping("/chats")
@AllArgsConstructor
public class ChatController {

    private final ChatService service;

    @Operation(summary = "Get chat by id")
    @GetMapping("/{id}")
    public DetailedChatDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Create chat")
    @PostMapping
    public ResponseEntity<DetailedChatDtoResponse> createEntity(@Valid @RequestBody ChatDtoRequest dtoRequest) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return new ResponseEntity<>(service.createEntity(dtoRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update chat")
    @PreAuthorize("hasRole('ADMIN') or @chatServiceImpl.isOwnerOrEmpty(#id)")
    @PutMapping("/{id}")
    public DetailedChatDtoResponse updateEntity(@Valid @RequestBody ChatDtoRequest dtoRequest,
                                                @PathVariable("id") @Min(1) Long id) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Delete chat")
    @PreAuthorize("hasRole('ADMIN') or @chatServiceImpl.isOwnerOrEmpty(#id)")
    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id) throws EntityNotFoundException {
        service.deleteUserAndProfile(id);
        return HttpStatus.NO_CONTENT;
    }

    @Operation(summary = "Add profile to chat by id")
    @PostMapping("{id}/members")
    public DetailedChatDtoResponse joinChat(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.joinChat(id);

    }

    @Operation(summary = "Remove profile from chat by id")
    @DeleteMapping("{id}/members")
    public DetailedChatDtoResponse leaveChat(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.leaveChat(id);
    }

    @Operation(summary = "Search chat by name, owned_id and member_id",
            description = "Case independent search chat by part of chat name, owned_id and member_id, " +
                    "pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<DetailedChatDtoResponse> searchChats(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "owned_id", defaultValue = "0", required = false) Long ownerId,
            @RequestParam(value = "member_id", defaultValue = "0", required = false) Long memberId) {
        return service.searchChats(pageSize, pageNumber, name, ownerId, memberId);
    }
}
