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
@Table(name = "subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Subscription {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "follower_id")
  private long followerId;
  @Column(name = "following_id")
  private long followingId;
}
