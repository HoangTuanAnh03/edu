package com.huce.edu_v2.controller;

import com.huce.edu_v2.entity.ChatEntity;
import com.huce.edu_v2.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
@AllArgsConstructor
//@CrossOrigin("*")
public class ChatController {
	final ChatService chatService;
	@GetMapping("/")
	public String hello(){
		return "hello";
	}

	@GetMapping("/users")
	public ResponseEntity<List<Long>> getAllUsers() {
		List<Long> userIds = chatService.getAllUsers();
		return ResponseEntity.ok(userIds);
	}

	@GetMapping("/users/getAllUserIdsAndLatestMessage")
	public ResponseEntity<List<Map<Object, Object>>> getAllUserIdsAndLatestMessage(){
		List<Object[]> res = chatService.findAllUserIdsAndLatestMessage();
		List<Map<Object, Object>> allUserIdAndLatestMessage = new ArrayList<>();
		res.stream().forEach(t -> {
			Map<Object, Object> userIdAndLatestMessage = new HashMap<>();
			userIdAndLatestMessage.put("id", t[0]);
			userIdAndLatestMessage.put("message", t[1]);
			allUserIdAndLatestMessage.add(userIdAndLatestMessage);
		});
		return ResponseEntity.ok(allUserIdAndLatestMessage);
	}

	@GetMapping("/messages/{userId}")
	public ResponseEntity<List<ChatEntity>> getMessagesByUserId(@PathVariable Long userId) {
		List<ChatEntity> messages = chatService.getMessagesByUserId(userId);
		return ResponseEntity.ok(messages);
	}
}