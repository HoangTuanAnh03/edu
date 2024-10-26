package com.huce.edu_v2.service;

import com.huce.edu_v2.dto.request.chat.MessageRequest;
import com.huce.edu_v2.dto.response.chat.UserResponse;
import com.huce.edu_v2.entity.Chat;

import java.util.List;

public interface ChatService {
	List<Chat> getMessagesByUserId(String userId);

	List<UserResponse> findAllUserIdsAndLatestMessage();

	Chat sendMessageToUser(String userId, MessageRequest messageRequest);

	Chat sendMessageToAdmin(MessageRequest messageRequest);
}