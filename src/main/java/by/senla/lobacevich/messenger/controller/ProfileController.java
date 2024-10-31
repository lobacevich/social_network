package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.exception.AuthorizationException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/profiles")
@AllArgsConstructor
public class ProfileController {

    private static final String IS_OWNER = """
            @profileRepository.findById(#id).isEmpty() or
            @profileRepository.findById(#id).get().user.username == authentication.name
            """;

    private final ProfileService service;

    @Operation(summary = "Get all profiles",
            description = "Pagination available using 'page_size' and 'page_number'")
    @GetMapping
    public List<DetailedProfileDtoResponse> findAll(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @Operation(summary = "Get profile by id")
    @GetMapping("/{id}")
    public DetailedProfileDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @Operation(summary = "Create profile")
    @PostMapping
    public ResponseEntity<DetailedProfileDtoResponse> createEntity(@Valid @RequestBody ProfileDtoRequest dtoRequest) throws EntityNotFoundException, InvalidDataException, AuthorizationException {
        return new ResponseEntity<>(service.createEntity(dtoRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update profile")
    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @PutMapping("/{id}")
    public DetailedProfileDtoResponse updateEntity(@Valid @RequestBody ProfileDtoRequest dtoRequest, @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException {
        return service.updateEntity(dtoRequest, id);
    }

    @Operation(summary = "Delete profile")
    @PreAuthorize("hasRole('ADMIN') or " + IS_OWNER)
    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id)  {
        service.deleteEntity(id);
        return HttpStatus.NO_CONTENT;
    }

    @Operation(summary = "Search profile by username, firstname and lastname",
            description = "Get profiles where part of username, firstname or last name likes case independent query, " +
                    "pagination available using 'page_size' and 'page_number'")
    @GetMapping("/search")
    public List<DetailedProfileDtoResponse> searchProfiles(
            @RequestParam("query") String query,
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.searchProfiles(query, pageSize, pageNumber);
    }

    @Operation(summary = "Get all groups, where authenticated user is member")
    @GetMapping("/groups")
    public List<DetailedGroupDtoResponse> getProfileGroups(Principal principal) throws EntityNotFoundException {
        return service.getProfileGroups(principal);
    }

    @Operation(summary = "Get all groups, owned by authenticated user")
    @GetMapping("/owned-groups")
    public List<DetailedGroupDtoResponse> getProfileGroupsOwned(Principal principal) throws EntityNotFoundException {
        return service.getProfileGroupsOwned(principal);
    }

    @Operation(summary = "Get all chats, where authenticated user is member")
    @GetMapping("/chats")
    public List<DetailedChatDtoResponse> getProfileChats(Principal principal) throws EntityNotFoundException {
        return service.getProfileChats(principal);
    }

    @Operation(summary = "Get all chats, owned by authenticated user")
    @GetMapping("/owned-chats")
    public List<DetailedChatDtoResponse> getProfileChatsOwned(Principal principal) throws EntityNotFoundException {
        return service.getProfileChatsOwned(principal);
    }

    @Operation(summary = "Get all posts of authenticated user")
    @GetMapping("/posts")
    public List<DetailedPostDtoResponse> getProfilePosts(Principal principal) throws EntityNotFoundException {
        return service.getProfilePosts(principal);
    }

    @Operation(summary = "Get all messages of authenticated user")
    @GetMapping("/messages")
    public List<MessageDtoResponse> getProfileMessages(Principal principal) throws EntityNotFoundException {
        return service.getProfileMessages(principal);
    }

    @Operation(summary = "Get all comments of authenticated user")
    @GetMapping("/comments")
    public List<CommentDtoResponse> getProfileComments(Principal principal) throws EntityNotFoundException {
        return service.getProfileComments(principal);
    }

    @Operation(summary = "Get all friends profiles of authenticated user")
    @GetMapping("/friends")
    public List<DetailedProfileDtoResponse> getProfileFriends(Principal principal) throws EntityNotFoundException {
        return service.getProfileFriends(principal);
    }

    @Operation(summary = "Get all no approved friendship requests to authenticated user")
    @GetMapping("/received-requests")
    public List<RequestFriendshipDtoResponse> getProfileReceivedFriendRequests(Principal principal) throws EntityNotFoundException {
        return service.getProfileReceivedNotApprovedFriendRequests(principal);
    }

    @Operation(summary = "Get all no approved friendship requests send by authenticated user")
    @GetMapping("/send-requests")
    public List<RequestFriendshipDtoResponse> getProfileSendFriendRequests(Principal principal) throws EntityNotFoundException {
        return service.getProfileReceivedNotApprovedFriendRequests(principal);
    }
}
