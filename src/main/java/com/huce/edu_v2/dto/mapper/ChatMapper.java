package com.huce.edu_v2.dto.mapper;

import com.huce.edu_v2.advice.exception.ResourceNotFoundException;
import com.huce.edu_v2.dto.response.chat.ChatResponse;
import com.huce.edu_v2.dto.response.chat.ReplyResponse;
import com.huce.edu_v2.dto.response.chat.UserResponse;
import com.huce.edu_v2.entity.Chat;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.repository.ChatRepository;
import com.huce.edu_v2.service.UserService;
import com.huce.edu_v2.util.constant.ChatTypeEnum;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMapper {
    UserService userService;
    ChatRepository chatRepository;

    public ChatResponse toChatResponse(Chat chat) {
        User user = userService.fetchUserById(chat.getUserId());
        User admin = userService.fetchUserById(chat.getAdminId());

        return ChatResponse.builder()
                .id(chat.getId())
                .userId(chat.getUserId())
                .userName(user.getName())
                .adminId(chat.getAdminId())
                .senderType(chat.getSenderType())
                .adminName(admin != null ? admin.getName() : "")
                .message(chat.getMessage())
                .timestamp(chat.getTimestamp())
                .status(chat.getStatus()).type(chat.getReplyId() == null ? ChatTypeEnum.SENT : ChatTypeEnum.REPLY)
                .reply(chat.getReplyId() == null
                        ? null
                        : toReplyResponse(chatRepository.findById(chat.getReplyId())
                                .orElseThrow(() -> new ResourceNotFoundException("Chat", "chatId", chat.getId()))))
                .build();
    }

    public ReplyResponse toReplyResponse(Chat chat) {
        User user = userService.fetchUserById(chat.getUserId());
        User admin = userService.fetchUserById(chat.getAdminId());

        return ReplyResponse.builder()
                .id(chat.getId())
                .senderId(admin != null ? admin.getId() : user.getId())
                .senderName(admin != null ? admin.getName() : user.getName())
                .message(chat.getMessage())
                .build();
    }

    public UserResponse toUserResponse(Chat chat) {
        User user = userService.fetchUserById(chat.getUserId());
        User admin = userService.fetchUserById(chat.getAdminId());

        return UserResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .image(user.getImage())
                .adminId(chat.getAdminId())
                .adminName(admin != null ? admin.getName() : "")
                .message(chat.getMessage())
                .senderType(chat.getSenderType())
                .status(chat.getStatus())
                .timestamp(chat.getTimestamp())
                .build();
    }
}
