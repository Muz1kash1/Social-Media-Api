package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.FriendsService;
import com.example.socialmediaapi.model.dto.ChatRequestDto;
import com.example.socialmediaapi.model.dto.FriendRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.nio.file.AccessDeniedException;

@RestController
@AllArgsConstructor
@Tag(name = "Ендпоинты взаимодействия пользователей")
public class FriendsController {
  final private FriendsService friendsService;

  @Operation(summary = "Послать запрос на дружбу", description = "Посылает запрос на дружбу")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Запрос создан"),
    @ApiResponse(responseCode = "403", description = "доступ запрещен")
  })
  @SecurityRequirement(name = "JWT token")
  @PostMapping("/friend-requests")
  public ResponseEntity<FriendRequestDto> sendFriendRequest(@RequestParam long receiverId,
                                                            JwtAuthenticationToken principal) {
    FriendRequestDto friendRequestDto = friendsService.sendFriendRequest(receiverId, principal.getName());
    return ResponseEntity.created(URI.create("/friend-requests/" + friendRequestDto.getId())).body(friendRequestDto);
  }

  @Operation(summary = "Принять запрос в друзья", description = "Делает запрос в друзья принятым")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Запрос принят"),
    @ApiResponse(responseCode = "403", description = "доступ запрещен")
  })
  @SecurityRequirement(name = "JWT token")
  @PutMapping("/friend-requests/{requestId}/accept")
  public ResponseEntity<FriendRequestDto> acceptFriendRequest(@PathVariable long requestId,
                                                              JwtAuthenticationToken principal)
    throws AccessDeniedException {
    FriendRequestDto friendRequestDto = friendsService.acceptFriendRequest(requestId, principal.getName());
    return ResponseEntity.ok().body(friendRequestDto);
  }
  @Operation(summary = "Отклонить запрос на дружбу", description = "Делает запрос в друзья отклонённым")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Запрос отклонён"),
    @ApiResponse(responseCode = "403", description = "доступ запрещен")
  })
  @SecurityRequirement(name = "JWT token")
  @PutMapping("/friend-requests/{requestId}/reject")
  public ResponseEntity<FriendRequestDto> rejectFriendRequest(@PathVariable long requestId,
                                                              JwtAuthenticationToken principal)
    throws AccessDeniedException {
    FriendRequestDto friendRequestDto = friendsService.rejectFriendRequest(requestId, principal.getName());
    return ResponseEntity.ok().body(friendRequestDto);
  }
  @Operation(summary = "Удалить запрос на дружбу", description = "Удаляет запрос на дружбу")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Запрос удалён"),
    @ApiResponse(responseCode = "403", description = "доступ запрещен")
  })
  @SecurityRequirement(name = "JWT token")
  @DeleteMapping("/friend-requests/{requestId}")
  public ResponseEntity<Void> deleteFriendRequest(@PathVariable long requestId,
                                                  JwtAuthenticationToken principal) throws AccessDeniedException {
    friendsService.deleteFriendRequest(requestId, principal.getName());
    return ResponseEntity.noContent().build();
  }
  @Operation(summary = "Удалить пользователя из друзей", description = "Удаляет пользователя из друзей")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Друг удалён"),
    @ApiResponse(responseCode = "403", description = "доступ запрещен")
  })
  @SecurityRequirement(name = "JWT token")
  @DeleteMapping("/friends/{friendId}")
  public ResponseEntity<Void> deleteFriend(@PathVariable long friendId,
                                           JwtAuthenticationToken principal) throws AccessDeniedException {
    friendsService.deleteFriend(friendId, principal.getName());
    return ResponseEntity.noContent().build();
  }
  @Operation(summary = "Послать запрос на переписку", description = "Посылает запрос на переписку")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Запрос создан"),
    @ApiResponse(responseCode = "403", description = "доступ запрещен")
  })
  @SecurityRequirement(name = "JWT token")
  @PostMapping("/chat-requests")
  public ResponseEntity<ChatRequestDto> sendChatRequest(@RequestParam long receiverId,
                                                        JwtAuthenticationToken principal) throws AccessDeniedException {
    ChatRequestDto chatRequestDto = friendsService.sendChatRequest(receiverId, principal.getName());
    return ResponseEntity.created(URI.create("/chat-requests/" + chatRequestDto.getId())).body(chatRequestDto);
  }
}
