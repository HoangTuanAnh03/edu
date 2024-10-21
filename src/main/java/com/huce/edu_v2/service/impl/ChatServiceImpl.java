package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.entity.ChatEntity;
import com.huce.edu_v2.repository.ChatRepository;
import com.huce.edu_v2.service.ChatService;
import com.huce.edu_v2.util.constant.SenderType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatServiceImpl implements ChatService {
	ChatRepository chatRepository;

	@Override
	public List<Long> getAllUsers() {
		return chatRepository.findAllUserIds();
	}

	@Override
	public List<ChatEntity> getMessagesByUserId(Long userId) {
		return chatRepository.findByUserId(userId);
	}

	@Override
	public List<Object[]> findAllUserIdsAndLatestMessage(){
		return chatRepository.findAllUserIdsAndLatestMessage();
	}

	@Override
	public void sendMessageToUser(Long userId, ChatEntity message) {
		message.setUserId(userId);
		message.setSenderId(userId); // chưa có jwt để lấy id admin. sau đổi thành id admin
		message.setSenderType(SenderType.ADMIN);
		chatRepository.save(message);
	}
}
