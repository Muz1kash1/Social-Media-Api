package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.FriendsService;
import com.example.socialmediaapi.model.dto.ChatRequestDto;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@AllArgsConstructor
public class FriendsController {
  final private FriendsService friendsService;

  @PostMapping("/friend-requests")
  public ResponseEntity<FriendRequestDto> sendFriendRequest(@RequestBody long receiverId,
                                                            JwtAuthenticationToken principal) {
    FriendRequestDto friendRequestDto = friendsService.sendFriendRequest(receiverId, principal.getName());
    return ResponseEntity.created(URI.create("/friend-requests/" + friendRequestDto.getId())).body(friendRequestDto);
  }

  @PutMapping("/friend-requests/{requestId}/accept")
  public ResponseEntity<FriendRequestDto> acceptFriendRequest(@PathVariable long requestId,
                                                              JwtAuthenticationToken principal) {
    FriendRequestDto friendRequestDto = friendsService.acceptFriendRequest(requestId, principal.getName());
    return ResponseEntity.ok().body(friendRequestDto);
  }

  @PutMapping("/friend-requests/{requestId}/reject")
  public ResponseEntity<FriendRequestDto> rejectFriendRequest(@PathVariable long requestId,
                                                              JwtAuthenticationToken principal) {
    FriendRequestDto friendRequestDto = friendsService.rejectFriendRequest(requestId, principal.getName());
    return ResponseEntity.ok().body(friendRequestDto);
  }

  @DeleteMapping("/friend-requests/{requestId}")
  public ResponseEntity<Void> deleteFriendRequest(@PathVariable long requestId,
                                                  JwtAuthenticationToken principal) {
    friendsService.deleteFriendRequest(requestId,principal.getName());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/friends/{friendId}")
  public ResponseEntity<Void> deleteFriend(@PathVariable long friendId,
                                           JwtAuthenticationToken principal) {
    friendsService.deleteFriend(friendId, principal.getName());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/chat-requests")
  public ResponseEntity<ChatRequestDto> sendChatRequest(@RequestBody long receiverId,
                                                          JwtAuthenticationToken principal){
    ChatRequestDto chatRequestDto = friendsService.sendChatRequest(receiverId,principal.getName());
    return ResponseEntity.created(URI.create("/chat-requests/" + chatRequestDto.getId())).body(chatRequestDto);
  }
}
