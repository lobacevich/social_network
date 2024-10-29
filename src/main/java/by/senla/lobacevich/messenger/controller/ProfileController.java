package by.senla.lobacevich.messenger.controller;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.ChatDtoResponse;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.InvalidDataException;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;
import by.senla.lobacevich.messenger.service.ProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ProfileService service;

    @GetMapping
    public List<DetailedProfileDtoResponse> findAll(
            @RequestParam(value = "page_size", defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = "page_number", defaultValue = "0", required = false) int pageNumber) {
        return service.findAll(pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public DetailedProfileDtoResponse findById(@PathVariable("id") Long id) throws EntityNotFoundException {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<DetailedProfileDtoResponse> createEntity(@Valid @RequestBody ProfileDtoRequest dtoRequest) throws EntityNotFoundException, InvalidDataException {
        return new ResponseEntity<>(service.createEntity(dtoRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public DetailedProfileDtoResponse updateEntity(@Valid @RequestBody ProfileDtoRequest dtoRequest, @PathVariable("id") Long id) throws EntityNotFoundException, InvalidDataException {
        return service.updateEntity(dtoRequest, id);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteEntity(@PathVariable("id") Long id)  {
        service.deleteEntity(id);
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/groups")
    public List<DetailedGroupDtoResponse> getProfileGroups(Principal principal) throws EntityNotFoundException {
        return service.getProfileGroups(principal);
    }

    @GetMapping("/groups-owned")
    public List<DetailedGroupDtoResponse> getProfileGroupsOwned(Principal principal) throws EntityNotFoundException {
        return service.getProfileGroupsOwned(principal);
    }

    @GetMapping("/chats")
    public List<DetailedChatDtoResponse> getProfileChats(Principal principal) throws EntityNotFoundException {
        return service.getProfileChats(principal);
    }

    @GetMapping("/posts")
    public List<DetailedPostDtoResponse> getProfilePosts(Principal principal) throws EntityNotFoundException {
        return service.getProfilePosts(principal);
    }

    @GetMapping("/messages")
    public List<MessageDtoResponse> getProfileMessages(Principal principal) throws EntityNotFoundException {
        return service.getProfileMessages(principal);
    }

    @GetMapping("/comments")
    public List<CommentDtoResponse> getProfileComments(Principal principal) throws EntityNotFoundException {
        return service.getProfileComments(principal);
    }

    @GetMapping("/friends")
    public List<DetailedProfileDtoResponse> getProfileFriends(Principal principal) throws EntityNotFoundException {
        return service.getProfileFriends(principal);
    }

    @GetMapping("/received-requests")
    public List<RequestFriendshipDtoResponse> getProfileReceivedFriendRequests(Principal principal) throws EntityNotFoundException {
        return service.getProfileReceivedFriendRequests(principal);
    }

    @GetMapping("/send-requests")
    public List<RequestFriendshipDtoResponse> getProfileSendFriendRequests(Principal principal) throws EntityNotFoundException {
        return service.getProfileReceivedFriendRequests(principal);
    }
}
