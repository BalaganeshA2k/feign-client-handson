package com.example.service1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Service1Application {
	private List<Message> messageRepo = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(Service1Application.class, args);
	}

	@GetMapping("/messages")
	public List<Message> getAllMessage() {
		return messageRepo;
	}

	@GetMapping("/message/{id}")
	public ResponseEntity<Message> getMessageById(@PathVariable int id) {
		var message = messageRepo.stream().filter(msg -> msg.id() == id).findFirst();
		if (message.isEmpty())
			return ResponseEntity.status(404).build();
		return ResponseEntity.ok(message.get());

	}

	@PostMapping("/message")
	public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
		messageRepo.add(message);
		return ResponseEntity.status(HttpStatus.CREATED).body(message);
	}

	@DeleteMapping("/message/{id}")
	public ResponseEntity deleteMessage(@PathVariable int id) {
		var message = messageRepo.stream().filter(msg -> msg.id() == id).findFirst();
		if (message.isEmpty())
			return ResponseEntity.status(404).build();
		messageRepo.remove(message.get());
		return ResponseEntity.ok().build();
	}

	public record Message(int id, String message) {
	}

}
