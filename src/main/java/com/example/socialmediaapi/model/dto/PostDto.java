package com.example.socialmediaapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDto {
  private final long id;
  private final long userId;
  private final String title;
  private final String text;
  private final LocalDateTime createdAt;
  private final List<String> pictures;

}
