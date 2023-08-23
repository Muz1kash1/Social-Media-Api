package com.example.socialmediaapi.infrastructure.mappers;

import com.example.socialmediaapi.model.domain.ChatRequest;
import com.example.socialmediaapi.model.dto.ChatRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRequestMapper {
  ChatRequestDto chatRequestToChatRequestDto(ChatRequest chatRequest);

  ChatRequest chatRequestDtoToChatRequest(ChatRequestDto chatRequestDto);

  ChatRequest chatRequestEntityToChatRequest(
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.ChatRequest chatRequest);

  com.example.socialmediaapi.infrastructure.repositories.entity.postgres.ChatRequest chatRequestToChatRequestEntity(
    ChatRequest chatRequest);
}
