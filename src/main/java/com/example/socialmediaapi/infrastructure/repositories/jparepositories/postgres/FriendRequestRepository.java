package com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres;

import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
  FriendRequest findTopByOrderByIdDesc();
}
