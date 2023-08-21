package com.example.socialmediaapi.infrastructure.controller;

import com.example.socialmediaapi.infrastructure.service.SubscriptionService;
import com.example.socialmediaapi.model.dto.SubscriptionDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@AllArgsConstructor
public class SubscriptionController {
  private final SubscriptionService subscriptionService;

  @PostMapping("/subscriptions")
  ResponseEntity<SubscriptionDto> subscribeForUser(@RequestBody long followingId,
                                                   JwtAuthenticationToken principal) {
    SubscriptionDto subscriptionDto = subscriptionService.subscribeForUser(followingId, principal.getName());
    return ResponseEntity.created(URI.create("/subscriptions/" + subscriptionDto.getId())).body(subscriptionDto);
  }
  @DeleteMapping("/subscriptions")
  ResponseEntity<Void> unsubscribeFromUser(@RequestBody long followingId,
                                           JwtAuthenticationToken principal){
    subscriptionService.unsubscribeFromUser(followingId,principal);
    return ResponseEntity.noContent().build();
  }
}
