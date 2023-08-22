package com.example.socialmediaapi.infrastructure.mappers;

import com.example.socialmediaapi.model.domain.Subscription;
import com.example.socialmediaapi.model.dto.SubscriptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
  SubscriptionDto subscriptionToSubscriptionDto(Subscription subscription);

  Subscription subscriptionDtoToSubscription(SubscriptionDto subscriptionDto);

  Subscription subscriptionEntityToSubscription(
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription subscriptionEntity);

  com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription subscriptionToSubscriptionEntity(
    Subscription subscription);

}
