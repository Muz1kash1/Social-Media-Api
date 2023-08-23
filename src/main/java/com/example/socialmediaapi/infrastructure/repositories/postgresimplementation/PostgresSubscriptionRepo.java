package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.SubscriptionMapper;
import com.example.socialmediaapi.infrastructure.repositories.ISubscriptionRepo;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.SubscriptionRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.Subscription;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import java.nio.file.AccessDeniedException;

@AllArgsConstructor
@Repository
@Slf4j
public class PostgresSubscriptionRepo implements ISubscriptionRepo {
  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final SubscriptionMapper subscriptionMapper;

  @Override public Subscription createSubscription(final long followingId, final String name) {
    Subscription subscription = new Subscription(
      0L,
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

  @Override public void removeSubscription(final String name, final long friendId) throws AccessDeniedException {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription subscription
      = subscriptionRepository.getSubscriptionByFollowerIdAndFollowingId(user.getId(), friendId);
    log.info(subscription.getFollowerId() + " " + subscription.getFollowingId());
    if (subscription.getFollowerId() == user.getId()) {
      subscriptionRepository.deleteById(subscription.getId());
    } else {
      throw new AccessDeniedException("Пользователь с именем " + name + " не является подписчиком");
    }
  }
}
