package com.huce.edu_v2.entity;

import com.huce.edu_v2.util.constant.SenderType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "chat")
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Enumerated(EnumType.STRING)
	SenderType senderType;

	String senderId;

	String recipientId;

	String message;

	LocalDateTime timestamp;

	@PrePersist
	protected void onCreate() {
		this.timestamp = LocalDateTime.now();
	}
}
