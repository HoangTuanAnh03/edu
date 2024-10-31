package com.huce.edu_v2.dto.mapper;

import com.huce.edu_v2.dto.response.chat.ChatResponse;
import com.huce.edu_v2.dto.response.chat.UserResponse;
import com.huce.edu_v2.entity.Chat;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMapper {
    UserService userService;

    public ChatResponse toChatResponse(Chat chat) {

        return ChatResponse.builder()
                .id(chat.getId())
                .userId(chat.getUserId())
                .adminId(chat.getAdminId())
                .message(chat.getMessage())
                .senderType(chat.getSenderType())
                .timestamp(chat.getTimestamp())
                .status(chat.getStatus())
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
