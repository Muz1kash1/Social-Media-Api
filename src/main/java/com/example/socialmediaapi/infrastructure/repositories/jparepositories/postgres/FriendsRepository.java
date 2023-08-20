package com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres;

import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friendship, Long> {
}
