package com.huce.edu_v2.controller;

import com.huce.edu_v2.entity.ChatEntity;
import com.huce.edu_v2.repository.ChatRepository;
import com.huce.edu_v2.service.ChatService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
	 ChatRepository chatRepository;
	 ChatService chatService;

	@MessageMapping("/userToAdmin")
	@SendTo("/topic/admin")
	public ChatEntity sendToAdmin(ChatEntity chat) {
		chat.setSenderId(chat.getUserId());
		chatRepository.save(chat);
		return chat;
	}

	@MessageMapping("/adminToUser/{userId}")
	public void sendToUser(@DestinationVariable Long userId, ChatEntity chat) {
		chatService.sendMessageToUser(userId, chat);
		messagingTemplate.convertAndSend("/topic/user/" + userId, chat);
		messagingTemplate.convertAndSend("/topic/admin", chat);
	}
}