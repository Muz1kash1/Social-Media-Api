package com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres;

import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.ChatRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long> {
  ChatRequest findTopByOrderByIdDesc();
}
