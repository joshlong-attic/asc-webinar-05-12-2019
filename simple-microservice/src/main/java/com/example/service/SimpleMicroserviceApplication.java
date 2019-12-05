package com.example.simplemicroservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@SpringBootApplication
public class SimpleMicroserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimpleMicroserviceApplication.class, args);
  }

  @Bean
  RouterFunction<ServerResponse> routes(
      CustomerRepository repository,
//      DiscoveryClient dc,
      @Value("${spring.data.mongodb.uri:}") String mongoDbUri,
      @Value("${application.message:}") String message) {
    return route()
        .GET("/message", r -> ok().bodyValue(message))
        .GET("/mongo", r -> ok().bodyValue(mongoDbUri))
//        .GET("/clients", r -> ok().bodyValue(this.hostsAndPorts(dc)))
        .GET("/customers", request -> ok().body(repository.findAll(), Customer.class))
        .build();
  }

  private Collection<String> hostsAndPorts(DiscoveryClient dc) {
    return dc.getInstances("simple-microservice")
        .stream()
        .map(si -> si.getUri().toString())
        .collect(Collectors.toList());
  }

}

interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
  @Id
  private String id;
  private String name;
}