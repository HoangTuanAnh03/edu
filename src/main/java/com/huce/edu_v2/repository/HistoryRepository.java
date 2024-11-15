package com.huce.edu_v2.repository;

import com.huce.edu_v2.entity.History;
import com.huce.edu_v2.entity.Topic;
import com.huce.edu_v2.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer>, JpaSpecificationExecutor<Topic> {
	 History findFirstByUidAndWord(String uid, Word word);
}
