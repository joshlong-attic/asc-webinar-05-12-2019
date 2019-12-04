package com.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Log4j2
@SpringBootApplication
public class SimpleMicroserviceApplication {

  @Bean
  RouterFunction<ServerResponse> routes(CustomerRepository customerRepository) {
    return route()
        .GET("/customers", req -> {
          log.info("returning all the " + Customer.class.getName() + " instances.");
          return ok().body(customerRepository.findAll(), Customer.class);
        })
        .GET("/hello", r -> ok().bodyValue(Collections.singletonMap("greeting", "Hello, world!")))
        .build();
  }

  @Bean
  ApplicationListener<ApplicationReadyEvent> ready(CustomerRepository repository) {
    return event -> Flux
        .just("A", "B", "C")
        .map(name -> new Customer(null, name))
        .flatMap(repository::save)
        .subscribe(log::info);
  }

  public static void main(String[] args) {
    SpringApplication.run(SimpleMicroserviceApplication.class, args);
  }
}


interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
class Customer {

  @Id
  private String id;
  private String name;
}