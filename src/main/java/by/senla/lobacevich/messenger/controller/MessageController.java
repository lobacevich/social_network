package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.MessageDtoRequest;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.MessageService;
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
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService service;

    @Operation(summary = "Get all messages",
            description = "Pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<MessageDtoResponse> findAll(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @Operation(summary = "Get message by id")
    @GetMapping("/{id}")
    public MessageDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Create message")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public MessageDtoResponse createEntity(@Valid @RequestBody MessageDtoRequest dtoRequest) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return service.createEntity(dtoRequest);
    }

    @Operation(summary = "Update message")
    @PreAuthorize("hasRole('ADMIN') or @messageServiceImpl.isOwnerOrEmpty(#id)")
    @PutMapping("/{id}")
    public MessageDtoResponse updateEntity(@Valid @RequestBody MessageDtoRequest dtoRequest, @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Delete message")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @messageServiceImpl.isOwnerOrEmpty(#id)")
    @DeleteMapping("/{id}")
    public void deleteEntity(@PathVariable("id") Long id) throws EntityNotFoundException {
        service.deleteEntity(id);
    }
}
