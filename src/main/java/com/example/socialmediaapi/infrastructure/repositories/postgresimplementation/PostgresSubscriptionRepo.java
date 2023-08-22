package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.SubscriptionMapper;
import com.example.socialmediaapi.infrastructure.repositories.ISubscriptionRepo;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.SubscriptionRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class PostgresSubscriptionRepo implements ISubscriptionRepo {
  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final SubscriptionMapper subscriptionMapper;

  @Override public Subscription createSubscription(final long followingId, final String name) {
    Subscription subscription = new Subscription(
      1L,
      userRepository
        .findUserByUsername(name)
        .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"))
        .getId(),
      followingId
    );
    subscriptionRepository.save(subscriptionMapper.subscriptionToSubscriptionEntity(subscription));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription subscriptionToReturn
      = subscriptionRepository.findTopByOrderByIdDesc();

    return subscriptionMapper.subscriptionEntityToSubscription(subscriptionToReturn);
  }
}
