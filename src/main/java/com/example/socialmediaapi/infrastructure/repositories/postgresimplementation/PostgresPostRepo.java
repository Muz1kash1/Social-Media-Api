package com.example.socialmediaapi.infrastructure.repositories.postgresimplementation;

import com.example.socialmediaapi.infrastructure.mappers.PostMapper;
import com.example.socialmediaapi.infrastructure.repositories.IPostRepo;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Subscription;
import com.example.socialmediaapi.infrastructure.repositories.entity.postgres.User;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.PostRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.SubscriptionRepository;
import com.example.socialmediaapi.infrastructure.repositories.jparepositories.postgres.UserRepository;
import com.example.socialmediaapi.model.domain.Post;
import com.example.socialmediaapi.model.dto.PostCreationDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Slf4j
public class PostgresPostRepo implements IPostRepo {
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final PostMapper postMapper;

  @Override
  public Post createPost(final PostCreationDto postCreationDto, final String name) {
    log.info(postCreationDto.toString());

    Post post =
      new Post(
        0L,
        userRepository
          .findUserByUsername(name)
          .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"))
          .getId(),
        postCreationDto.getTitle(),
        postCreationDto.getText(),
        postCreationDto.getCreatedAt(),
        postCreationDto.getPictures()

      );
    log.info(post.toString());
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post postToSave =
      postMapper.postToPostEntity(post);

    postRepository.save(postToSave);

    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post postToReturn
      = postRepository.findTopByOrderByIdDesc();
    return postMapper.postEntityToPost(postToReturn);
  }

  @Override
  public Post redactPost(final long postId, final PostCreationDto postCreationDto, final String name)
    throws AccessDeniedException {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post post = postRepository
      .findById(postId)
      .orElseThrow(() -> new NoSuchElementException("Поста с id " + postId + " не существует"));

    if (post.getUserId() == user.getId()) {
      post.setTitle(postCreationDto.getTitle());
      post.setText(postCreationDto.getText());
      post.setPictures(postCreationDto.getPictures());

      postRepository.save(post);

      com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post postToReturn = postRepository
        .findById(post.getId())
        .orElseThrow(() -> new NoSuchElementException("Поста с id " + postId + " не существует"));

      return postMapper.postEntityToPost(postToReturn);

    } else {
      throw new AccessDeniedException("Пользователь с именем " + name + " не является создателем поста");
    }

  }

  @Override
  public void deletePost(final long postId, final String name) throws AccessDeniedException {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));
    com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post post = postRepository
      .findById(postId)
      .orElseThrow(() -> new NoSuchElementException("Поста с id " + postId + " не существует"));

    if (post.getUserId() == user.getId()) {
      postRepository.deleteById(postId);
    } else {
      throw new AccessDeniedException("Пользователь с именем " + name + " не является создателем поста");
    }
  }

  @Override public List<Post> getUserPosts(final long userId, final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<com.example.socialmediaapi.infrastructure.repositories.entity.postgres.Post> postPage =
      postRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);

    return postPage.getContent().stream()
      .map(postMapper::postEntityToPost)
      .collect(Collectors.toList());
  }

  @Override
  public List<Post> getFeed(final String name, final int page, final int size) {
    User user = userRepository
      .findUserByUsername(name)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователя с именем " + name + " не существует"));

    List<Subscription> subscriptions = subscriptionRepository.findAllByFollowerId(user.getId());

    return subscriptions.stream()
      .map(subscription -> getUserPosts(subscription.getFollowingId(), page, size))
      .flatMap(Collection::stream)
      .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
      .toList();

  }
}
