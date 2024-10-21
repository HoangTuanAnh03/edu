package com.huce.edu_v2.repository;

import com.huce.edu_v2.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
	@Query("SELECT DISTINCT cm.userId FROM ChatEntity cm ORDER BY cm.timestamp desc ")
	List<Long> findAllUserIds();
	List<ChatEntity> findByUserId(Long userId);
	@Query("SELECT cm.userId, cm.message " +
			"FROM ChatEntity cm " +
			"WHERE cm.timestamp = (SELECT MAX(subcm.timestamp) FROM ChatEntity subcm WHERE subcm.userId = cm.userId) " +
			"ORDER BY cm.timestamp DESC")
	List<Object[]> findAllUserIdsAndLatestMessage();
}