package com.example.socialmediaapi.infrastructure.repositories;

import com.example.socialmediaapi.model.domain.Subscription;
import java.nio.file.AccessDeniedException;

public interface ISubscriptionRepo {
  Subscription createSubscription(long followingId, String name);

  void removeSubscription(String name, long friendId) throws AccessDeniedException;
}
