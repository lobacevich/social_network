package by.senla.lobacevich.messenger.service;

import by.senla.lobacevich.messenger.dto.request.ProfileDtoRequest;
import by.senla.lobacevich.messenger.dto.response.CommentDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedChatDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedGroupDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedPostDtoResponse;
import by.senla.lobacevich.messenger.dto.response.DetailedProfileDtoResponse;
import by.senla.lobacevich.messenger.dto.response.MessageDtoResponse;
import by.senla.lobacevich.messenger.dto.response.RequestFriendshipDtoResponse;
import by.senla.lobacevich.messenger.entity.Profile;
import by.senla.lobacevich.messenger.exception.EntityNotFoundException;

import java.security.Principal;
import java.util.List;

public interface ProfileService extends GenericService<ProfileDtoRequest, DetailedProfileDtoResponse, Profile> {

    Profile getProfileByPrincipal(Principal principal) throws EntityNotFoundException;

    List<DetailedGroupDtoResponse> getProfileGroups(Principal principal) throws EntityNotFoundException;

    List<DetailedGroupDtoResponse> getProfileGroupsOwned(Principal principal) throws EntityNotFoundException;

    List<DetailedChatDtoResponse> getProfileChats(Principal principal) throws EntityNotFoundException;

    List<DetailedPostDtoResponse> getProfilePosts(Principal principal) throws EntityNotFoundException;

    List<MessageDtoResponse> getProfileMessages(Principal principal) throws EntityNotFoundException;

    List<CommentDtoResponse> getProfileComments(Principal principal) throws EntityNotFoundException;

    List<DetailedProfileDtoResponse> getProfileFriends(Principal principal) throws EntityNotFoundException;

    List<RequestFriendshipDtoResponse> getProfileReceivedFriendRequests(Principal principal) throws EntityNotFoundException;

    List<RequestFriendshipDtoResponse> getProfileSendFriendRequests(Principal principal) throws EntityNotFoundException;
}
