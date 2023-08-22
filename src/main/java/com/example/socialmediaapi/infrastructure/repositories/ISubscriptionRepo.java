package com.example.socialmediaapi.infrastructure.repositories;

import com.example.socialmediaapi.model.domain.Subscription;

public interface ISubscriptionRepo {
  Subscription createSubscription(long followingId, String name);
}
