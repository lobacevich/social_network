package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.CommentDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.CommentService;
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

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private static final String IS_OWNER = """
            @commentRepository.findById(#id).isEmpty() or
            @profileServiceImpl.getProfileByPrincipal(#principal).id ==
            @commentRepository.findById(#id).get().author.id
            """;

    private final CommentService service;

    @GetMapping
    public List<CommentDtoResponse> findAll(@RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize, @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public CommentDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<CommentDtoResponse> createEntity(@Valid @RequestBody CommentDtoRequest dtoRequest) throws EntityNotFoundException, InvalidDataException, AuthorizationException {
        return new ResponseEntity<>(service.createEntity(dtoRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @PutMapping("/{id}")
    public CommentDtoResponse updateEntity(@Valid @RequestBody CommentDtoRequest dtoRequest, @PathVariable("id") Long id, Principal principal) throws EntityNotFoundException, InvalidDataException {
        return service.updateEntity(dtoRequest, id);
    }

    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id, Principal principal) {
        service.deleteEntity(id);
        return HttpStatus.NO_CONTENT;
    }
}
