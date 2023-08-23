package com.example.socialmediaapi.infrastructure.repositories.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "sender_id")
  private long senderId;
  @Column(name = "receiver_id")
  private long receiverId;
  @Column(name = "status")
  private String status;
}
