package by.senla.lobacevich.messenger.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    @GetMapping("/{id}")
    public Long get(@PathVariable("id") Long id) {
        log.error(id);
        return id;
    }

    @GetMapping("/")
    public Principal getPr(Principal principal) {
        return principal;
    }
}
