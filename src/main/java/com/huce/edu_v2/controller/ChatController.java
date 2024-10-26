package com.huce.edu_v2.controller;

import com.huce.edu_v2.dto.ApiResponse;
import com.huce.edu_v2.dto.response.chat.UserResponse;
import com.huce.edu_v2.entity.Chat;
import com.huce.edu_v2.service.ChatService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infoChat")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
	ChatService chatService;

	@GetMapping("/users/getAllUserIdsAndLatestMessage")
	public ApiResponse<List<UserResponse>> getAllUserIdsAndLatestMessage(){
		List<UserResponse> res = chatService.findAllUserIdsAndLatestMessage();
		return ApiResponse.<List<UserResponse>>builder()
				.code(HttpStatus.OK.value())
				.message("Fetch all userIds and latest message")
				.data(res)
				.build();
	}

	@GetMapping("/messages/{userId}")
	public ApiResponse<List<Chat>> getMessagesByUserId(@PathVariable String userId) {
		List<Chat> messages = chatService.getMessagesByUserId(userId);
		return ApiResponse.<List<Chat>>builder()
				.code(HttpStatus.OK.value())
				.message("Fetch all message by userid")
				.data(messages)
				.build();
	}
}