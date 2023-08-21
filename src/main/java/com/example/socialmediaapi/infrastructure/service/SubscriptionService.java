package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.model.dto.SubscriptionDto;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
  public SubscriptionDto subscribeForUser(final long followingId, final String name) {
    return null;
  }

  public void unsubscribeFromUser(final long followingId, final JwtAuthenticationToken principal) {
  }
}
