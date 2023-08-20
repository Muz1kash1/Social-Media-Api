package com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres;

import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findUserById(long id);

  Optional<User> findUserByUsername(String username);

  Optional<Void> deleteById(long id);
}
