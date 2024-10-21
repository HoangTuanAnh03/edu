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
public class ChatEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "sender_type", nullable = false)
	private SenderType senderType;

	@Column(name = "sender_id", nullable = false)
	private Long senderId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "message", nullable = false)
	private String message;

	@Column(name = "timestamp", nullable = false)
	private LocalDateTime timestamp;

	@PrePersist
	protected void onCreate() {
		this.timestamp = LocalDateTime.now();
	}
}
