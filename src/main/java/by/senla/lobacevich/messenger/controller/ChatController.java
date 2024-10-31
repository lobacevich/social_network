package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.ChatDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
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

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/chats")
@AllArgsConstructor
public class ChatController {

    private static final String IS_OWNER = """
            @chatRepository.findById(#id).isEmpty() or
            @profileServiceImpl.getProfileByPrincipal(#principal).id ==
            @chatRepository.findById(#id).get().owner.id
            """;

    private final ChatService service;

    @Operation(summary = "Get all chats",
    description = "Pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<DetailedChatDtoResponse> findAll(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @Operation(summary = "Get chat by id")
    @GetMapping("/{id}")
    public DetailedChatDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Create chat")
    @PostMapping
    public ResponseEntity<DetailedChatDtoResponse> createEntity(@Valid @RequestBody ChatDtoRequest dtoRequest,
                                                                Principal principal) throws EntityNotFoundException, InvalidDataException, AuthorizationException {
        return new ResponseEntity<>(service.createEntity(dtoRequest, principal), HttpStatus.CREATED);
    }

    @Operation(summary = "Update chat")
    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @PutMapping("/{id}")
    public DetailedChatDtoResponse updateEntity(@Valid @RequestBody ChatDtoRequest dtoRequest,
                                                @PathVariable("id") @Min(1) Long id, Principal principal) throws EntityNotFoundException, InvalidDataException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Delete chat")
    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id, Principal principal) {
        service.deleteEntity(id);
        return HttpStatus.NO_CONTENT;
    }

    @Operation(summary = "Search chat by name",
    description = "Case independent search chat by part of chat name, pagination available using 'page_size' and 'page_number'")
    @GetMapping("/search")
    public List<DetailedChatDtoResponse> searchChats(
            @RequestParam("name") String name,
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.searchChats(name, pageSize, pageNumber);
    }

    @Operation(summary = "Add profile to chat by id")
    @PostMapping("{id}/members")
    public DetailedChatDtoResponse joinChat(@PathVariable("id") Long id, Principal principal) throws EntityNotFoundException {
        return service.joinChat(id, principal);

    }

    @Operation(summary = "Remove profile from chat by id")
    @DeleteMapping("{id}/members")
    public DetailedChatDtoResponse leaveChat(@PathVariable("id") Long id, Principal principal) throws EntityNotFoundException {
        return service.leaveChat(id, principal);
    }
}
