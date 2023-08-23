package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.SubscriptionMapper;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.SubscriptionRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.Subscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты методов репозитория для работы с подсписками")
class PostgresSubscriptionRepoTest {
  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SubscriptionMapper subscriptionMapper;

  @InjectMocks
  private PostgresSubscriptionRepo subscriptionRepo;

  @DisplayName("Тест метода создания подписки")
  @Test
  public void testCreateSubscription() {
    when(userRepository.findUserByUsername(anyString()))
      .thenReturn(Optional.of(new User(1L, "user1", "user1@example.com", "password")));
    when(subscriptionMapper.subscriptionToSubscriptionEntity(any(Subscription.class)))
      .thenReturn(new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription());

    when(subscriptionRepository.findTopByOrderByIdDesc())
      .thenReturn(new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription(1L, 1L, 2L));

    subscriptionRepo.createSubscription(2L, "user1");

    verify(subscriptionRepository, times(1)).save(
      any(com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription.class));
  }

  @DisplayName("Тест метода удаления подписки")
  @Test
  public void testRemoveSubscription() {
    User user = new User(1L, "user1", "user1@example.com", "password");
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription subscription =
      new com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription(1L, 1L, 2L);
    when(subscriptionRepository.getSubscriptionByFollowerIdAndFollowingId(anyLong(), anyLong()))
      .thenReturn(subscription);

    assertDoesNotThrow(() -> subscriptionRepo.removeSubscription("user1", 2L));

    verify(subscriptionRepository, times(1)).deleteById(eq(1L));
  }

}