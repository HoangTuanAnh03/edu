package com.huce.edu_v2.service;

import com.huce.edu_v2.entity.ChatEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChatService {
	public List<Long> getAllUsers();

	public List<ChatEntity> getMessagesByUserId(Long userId);

	public List<Object[]> findAllUserIdsAndLatestMessage();
	public void sendMessageToUser(Long userId, ChatEntity message);
}