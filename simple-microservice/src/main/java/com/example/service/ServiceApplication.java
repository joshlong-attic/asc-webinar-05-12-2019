package com.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;

@RestController
@SpringBootApplication
public class ServiceApplication {

  private final CustomerRepository repository;

  ServiceApplication(CustomerRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/greetings")
  Map<String, String> greetings() {
    return Collections
        .singletonMap("greeting", "hello, world!");
  }

  @GetMapping("/customers")
  Flux<Customer> customers() {
    return this.repository.findAll();
  }

  public static void main(String[] args) {
    SpringApplication.run(ServiceApplication.class, args);
  }
}

@Component
@RequiredArgsConstructor
class Initializer {

  private final CustomerRepository repository;

  @EventListener(ApplicationReadyEvent.class)
  public void ready() {
    Flux
        .just("A", "B", "C")
        .map(name -> new Customer(null, name))
        .flatMap(repository::save)
        .subscribe();
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