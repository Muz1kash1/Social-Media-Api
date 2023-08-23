package com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres;

import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
  Subscription findTopByOrderByIdDesc();
  List<Subscription> findAllByFollowerId(long id);
  void deleteByFollowerIdAndFollowingId(long followerId, long followingId);
  Subscription getSubscriptionByFollowerIdAndFollowingId(long followerId, long followingId);
}
