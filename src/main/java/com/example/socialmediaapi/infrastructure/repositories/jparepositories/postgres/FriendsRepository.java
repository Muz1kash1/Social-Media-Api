package com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres;

import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendsRepository extends JpaRepository<Friendship, Long> {
  @Modifying
  @Query(
    value = "DELETE FROM friends WHERE (user1_id = :id1 AND user2_id = :id2) OR (user1_id = :id2 AND user2_id = :id1)",
    nativeQuery = true)
  void deleteByUserIds(long id1, long id2);

  @Query(
    value = "SELECT * FROM friends WHERE (user1_id = :id1 AND user2_id = :id2) OR (user1_id = :id2 AND user2_id = :id1)",
    nativeQuery = true)
  Friendship getByUserIds(long id1, long id2);
}
