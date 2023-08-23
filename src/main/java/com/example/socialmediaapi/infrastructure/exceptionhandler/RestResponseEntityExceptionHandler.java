package com.example.socialmediaapi.infrastructure.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

import static org.zalando.problem.Status.FORBIDDEN;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static org.zalando.problem.Status.NOT_FOUND;

@ControllerAdvice
public class RestResponseEntityExceptionHandler{

  @ExceptionHandler({UsernameNotFoundException.class, NoSuchElementException.class})
  public ResponseEntity<Problem> handleNotFoundException(Exception ex) {
    Problem problem = Problem.builder()
      .withStatus(NOT_FOUND)
      .withTitle("Resource Not Found")
      .withDetail(ex.getMessage())
      .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.valueOf("application/problem+json")).body(problem);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Problem> handleAccessDeniedException(AccessDeniedException ex) {
    Problem problem = Problem.builder()
      .withStatus(FORBIDDEN)
      .withTitle("Access Denied")
      .withDetail(ex.getMessage())
      .build();
    return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.valueOf("application/problem+json")).body(problem);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Problem> handleGenericException(Exception ex) {
    Problem problem = Problem.builder()
      .withStatus(INTERNAL_SERVER_ERROR)
      .withTitle("Internal Server Error")
      .withDetail(ex.getMessage())
      .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.valueOf("application/problem+json")).body(problem);
  }
}
