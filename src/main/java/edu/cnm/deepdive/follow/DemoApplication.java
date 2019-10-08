package edu.cnm.deepdive.follow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the Follow application. All of the work of application startup is performed
 * (directly or indirectly) by the {@link SpringApplication} class, which initiates the process of
 * discovery/reflection to instantiate the components required by a Spring Data/Spring MVC
 * application.
 */
@SpringBootApplication
public class DemoApplication {

  /**
   * Main entry point for the follow-mixin Spring Boot application.
   */
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

}
