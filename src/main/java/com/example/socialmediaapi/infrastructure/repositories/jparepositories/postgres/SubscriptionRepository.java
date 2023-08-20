package com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres;

import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
}
