package com.example.socialmediaapi.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
  @NotNull
  private long id;
  @NotNull
  private long userId;
  @NotEmpty
  private String title;
  @NotEmpty
  private String text;
  @NotNull
  private LocalDateTime createdAt;
  @NotNull
  private List<String> pictures;
}
