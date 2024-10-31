package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.UserService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private static final String IS_OWNER = """
            @userRepository.findById(#id).isEmpty() or
            @userRepository.findById(#id).get().username == authentication.name
            """;

    private final UserService service;

    @Operation(summary = "Get all users",
            description = "Pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<UserDtoResponse> findAll(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public UserDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Update user")
    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @PutMapping("/{id}")
    public UserDtoResponse updateEntity(@Valid @RequestBody UserDtoRequest dtoRequest, @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Delete user")
    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id)  {
        service.deleteEntity(id);
        return HttpStatus.NO_CONTENT;
    }

    @Operation(summary = "Make user's role ROLE_ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/admin")
    public UserDtoResponse makeAdmin(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.makeAdmin(id);
    }

    @Operation(summary = "Make user's role ROLE_USER")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/user")
    public UserDtoResponse makeUser(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.makeUser(id);
    }
}
