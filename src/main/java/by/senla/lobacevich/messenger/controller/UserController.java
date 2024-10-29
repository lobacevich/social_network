package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.UserDtoResponse;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final UserService service;

    @GetMapping
    public List<UserDtoResponse> findAll(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public UserDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or #dtoRequest.id == @userServiceImpl.getUserByUsername(principal.username).id")
    @PutMapping("/{id}")
    public UserDtoResponse updateEntity(@Valid @RequestBody UserDtoRequest dtoRequest, @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException {
        return service.updateEntity(dtoRequest, id);
    }

    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.findById(#id).username == principal.username")
    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id)  {
        service.deleteEntity(id);
        return HttpStatus.NO_CONTENT;
    }
}
