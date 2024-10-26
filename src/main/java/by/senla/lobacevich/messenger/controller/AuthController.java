package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.UserDtoRequest;
import by.senla.lobacevich.messenger.dto.response.TextResponse;
import by.senla.lobacevich.messenger.dto.response.ValidationErrorResponse;
import by.senla.lobacevich.messenger.security.JWTTokenProvider;
import by.senla.lobacevich.messenger.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private final ValidationErrorResponse validationErrorResponse;

    @GetMapping("/login")
    private String get() {
        return "Login!";
    }

    @PostMapping("/signup")
    public Object registerUser(@Valid @RequestBody UserDtoRequest request,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationErrorResponse.setErrors(bindingResult);
        }
        return service.createEntity(request);
    }

    @PostMapping("/login")
    public Object authenticateUser(@Valid @RequestBody UserDtoRequest request,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validationErrorResponse.setErrors(bindingResult);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new TextResponse(TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication));
    }
}
