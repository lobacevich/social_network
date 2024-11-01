package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profiles")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @Operation(summary = "Get profile by id")
    @GetMapping("/{id}")
    public DetailedProfileDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Update profile")
    @PutMapping("/{id}")
    public DetailedProfileDtoResponse updateEntity(@Valid @RequestBody ProfileDtoRequest dtoRequest, @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException, AccessDeniedException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Search profile by username, firstname and lastname",
            description = "Get profiles where part of username, firstname or last name likes case independent query, " +
                    "pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<DetailedProfileDtoResponse> searchProfiles(
            @RequestParam(value = "query", defaultValue = "", required = false) String query,
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.searchProfiles(query, pageSize, pageNumber);
    }
}
