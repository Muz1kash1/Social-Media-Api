package com.example.socialmediaapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionDto {
  private final long id;
  private final long followerId;
  private final long followingId;
}
