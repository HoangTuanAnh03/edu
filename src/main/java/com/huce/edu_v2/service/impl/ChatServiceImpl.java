package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.advice.exception.ResourceNotFoundException;
import com.huce.edu_v2.dto.mapper.ChatMapper;
import com.huce.edu_v2.dto.request.chat.MessageRequest;
import com.huce.edu_v2.dto.response.chat.ChatResponse;
import com.huce.edu_v2.dto.response.chat.UserResponse;
import com.huce.edu_v2.entity.Chat;
import com.huce.edu_v2.repository.ChatRepository;
import com.huce.edu_v2.service.ChatService;
import com.huce.edu_v2.util.SecurityUtil;
import com.huce.edu_v2.util.constant.ChatStatusEnum;
import com.huce.edu_v2.util.constant.SenderType;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatServiceImpl implements ChatService {
	ChatRepository chatRepository;
	SecurityUtil securityUtil;
	ChatMapper chatMapper;

	@Override
	public List<ChatResponse> getMessagesByUserId(String userId) {
		return chatRepository.findByUserId(userId).stream().map(chatMapper::toChatResponse).toList();
	}

	@Override
	public UserResponse getUserByMessagesId(Long messagesId) {
		Chat chat = chatRepository.findById(messagesId).orElseThrow(
				() -> new ResourceNotFoundException("ChatEntity", "id", messagesId)
		);

		return chatMapper.toUserResponse(chat);
	}

	@Override
	public List<UserResponse> findAllUserIdsAndLatestMessage() {
		List<Object[]> res = chatRepository.findAllUserIdsAndLatestMessage();
		List<UserResponse> allUserIdAndLatestMessage = new ArrayList<>();

		res.forEach(t -> chatRepository.findById((Long) t[0]).ifPresent(chat -> allUserIdAndLatestMessage.add(chatMapper.toUserResponse(chat))));

		return allUserIdAndLatestMessage;
	}

	@SneakyThrows
	@Override
	public ChatResponse sendMessageToUser(String userId, MessageRequest messageRequest) {
		String uidByToken = securityUtil.getUuid(SignedJWT.parse(messageRequest.getAccessToken()));

		Chat newChat = chatRepository.save(Chat.builder()
				.senderType(SenderType.ADMIN)
				.userId(userId)
				.adminId(uidByToken)
				.message(messageRequest.getMessage())
				.status(ChatStatusEnum.SENT)
				.build());

		return chatMapper.toChatResponse(newChat);
	}

	@SneakyThrows
	@Override
	public ChatResponse sendMessageToAdmin(MessageRequest messageRequest) {
		String uidByToken = securityUtil.getUuid(SignedJWT.parse(messageRequest.getAccessToken()));

		Chat newChat = chatRepository.save(Chat.builder()
				.senderType(SenderType.USER)
				.userId(uidByToken)
				.adminId("")
				.message(messageRequest.getMessage())
				.status(ChatStatusEnum.SENT)
				.build());

		return chatMapper.toChatResponse(newChat);
	}
}
