package com.example.clientservice;

import static java.lang.System.out;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
@EnableFeignClients
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

	public record Message(int id, String message) {
	}

	@FeignClient(name = "feign-name", url = "http://localhost:9090")
	interface ExternalService1 {
		@GetMapping("/messages")
		public List<Message> getAllMessages();

		@GetMapping("/message/{id}")
		public ResponseEntity<Message> getById(@PathVariable int id);

		@PostMapping("/message")
		public ResponseEntity<Message> postMessage(Message message);

		@DeleteMapping("/message/{id}")
		public ResponseEntity<Void> deleteMessage(@PathVariable int id);
	}

	@Bean
	CommandLineRunner cli(ExternalService1 externalService1) {
		return arg -> {
			out.println("All Messages: " + externalService1.getAllMessages().toString());
			out.println("Posted Message: "
					+ externalService1.postMessage(new Message(0, "a new messsage from client")).getBody());

			out.println("Get Message by id: " + externalService1.getById(0).getStatusCode());
			out.println("Delete added message: " + externalService1.deleteMessage(0));
			out.println("All Messages: " + externalService1.getAllMessages());
		};

	}
}
