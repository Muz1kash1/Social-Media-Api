package com.example.socialmediaapi.infrastructure.repositories.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "user_id")
  private long userId;
  @Column(name = "title")
  private String title;
  @Column(name = "text")
  private String text;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "pictures")
  private List<String> pictures;

}
