package com.huce.edu_v2.controller;

import com.huce.edu_v2.dto.request.chat.MessageRequest;
import com.huce.edu_v2.dto.response.chat.ChatResponse;
import com.huce.edu_v2.service.ChatService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketChatController {
	SimpMessagingTemplate messagingTemplate;
	ChatService chatService;

	@MessageMapping("/userToAdmin/{userId}")
//	@SendTo("/topic/admin")
	public void sendToAdmin(@DestinationVariable String userId, MessageRequest messageRequest) {
//		return chatService.sendMessageToAdmin(messageRequest);
		ChatResponse chatResponse = chatService.sendMessageToAdmin(messageRequest);
		messagingTemplate.convertAndSend("/topic/user/" + userId, chatResponse);
		messagingTemplate.convertAndSend("/topic/admin", chatResponse);
	}

	@SneakyThrows
    @MessageMapping("/adminToUser/{userId}")
	public void sendToUser(@DestinationVariable String userId, MessageRequest messageRequest) {
		ChatResponse chatResponse = chatService.sendMessageToUser(userId, messageRequest);
		messagingTemplate.convertAndSend("/topic/user/" + userId, chatResponse);
		messagingTemplate.convertAndSend("/topic/admin", chatResponse);
	}
}