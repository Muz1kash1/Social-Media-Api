package com.example.socialmediaapi.infrastructure.service;

import com.example.socialmediaapi.infrastructure.mappers.SubscriptionMapper;
import com.example.socialmediaapi.infrastructure.repositories.ISubscriptionRepo;
import com.example.socialmediaapi.model.domain.Subscription;
import com.example.socialmediaapi.model.dto.SubscriptionDto;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {
  private final SubscriptionMapper subscriptionMapper;
  private final ISubscriptionRepo subscriptionRepo;

  public SubscriptionDto subscribeForUser(final long followingId, final String name) {
    Subscription subscription = subscriptionRepo.createSubscription(followingId, name);
    return subscriptionMapper.subscriptionToSubscriptionDto(subscription);
  }

  public void unsubscribeFromUser(final long followingId, final JwtAuthenticationToken principal) {
  }
}
