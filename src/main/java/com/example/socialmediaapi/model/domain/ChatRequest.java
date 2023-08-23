package com.example.socialmediaapi.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
  @NotNull
  private long id;
  @NotNull
  private long senderId;
  @NotNull
  private long receiverId;
  @NotEmpty
  private String status;
}
