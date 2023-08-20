package com.example.socialmediaapi;


import com.example.socialmediaapi.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SocialMediaApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(SocialMediaApiApplication.class, args);
  }

}
