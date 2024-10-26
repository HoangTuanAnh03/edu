package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.dto.request.chat.MessageRequest;
import com.huce.edu_v2.dto.response.chat.UserResponse;
import com.huce.edu_v2.entity.Chat;
import com.huce.edu_v2.repository.ChatRepository;
import com.huce.edu_v2.service.ChatService;
import com.huce.edu_v2.util.SecurityUtil;
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

	@Override
	public List<Chat> getMessagesByUserId(String recipientId) {
		return chatRepository.findByRecipientIdOrSenderId(recipientId, recipientId);
	}

	@Override
	public List<UserResponse> findAllUserIdsAndLatestMessage() {
		List<Object[]> res = chatRepository.findAllUserIdsAndLatestMessage();
		List<UserResponse> allUserIdAndLatestMessage = new ArrayList<>();

		res.forEach(t -> {
			if (t[1].equals("")) {
				if (allUserIdAndLatestMessage.stream().noneMatch(userResponse -> userResponse.getSenderId().equals(t[0]))) {
					allUserIdAndLatestMessage.add(UserResponse.builder().senderId(t[0].toString()).message(t[2].toString()).build());
				}
			}
			else if (allUserIdAndLatestMessage.stream().noneMatch(userResponse -> userResponse.getSenderId().equals(t[1]))) {
				allUserIdAndLatestMessage.add(UserResponse.builder().senderId(t[1].toString()).message(t[2].toString()).build());
			}
		});
		return allUserIdAndLatestMessage;
	}

	@SneakyThrows
	@Override
	public Chat sendMessageToUser(String recipientId, MessageRequest messageRequest) {
		String uidByToken = securityUtil.getUuid(SignedJWT.parse(messageRequest.getAccessToken()));

		return chatRepository.save(Chat.builder().senderType(SenderType.ADMIN).senderId(uidByToken).recipientId(recipientId).message(messageRequest.getMessage()).build());
	}

	@SneakyThrows
	@Override
	public Chat sendMessageToAdmin(MessageRequest messageRequest) {
		String uidByToken = securityUtil.getUuid(SignedJWT.parse(messageRequest.getAccessToken()));

		return chatRepository.save(Chat.builder().senderType(SenderType.USER).senderId(uidByToken).recipientId("").message(messageRequest.getMessage()).build());
	}
}
