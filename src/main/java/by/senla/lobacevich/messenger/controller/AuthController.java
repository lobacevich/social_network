package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.exception.AccessDeniedException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.security.JWTTokenProvider;
import by.senla.lobacevich.messenger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Log4j2
public class AuthController {

    public static final String TOKEN_PREFIX = "Bearer ";

    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @Operation(summary = "Register a new user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public DetailedProfileDtoResponse registerUser(@Valid @RequestBody UserDtoRequest request) throws InvalidDataException, EntityNotFoundException, AccessDeniedException {
        return service.createUserAndProfile(request);
    }

    @Operation(summary = "User log in")
    @PostMapping("/login")
    public String authenticateUser(@RequestBody UserDtoRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
    }
}
