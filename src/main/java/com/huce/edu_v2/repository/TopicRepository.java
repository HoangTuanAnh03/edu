package com.huce.edu_v2.repository;

import com.huce.edu_v2.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
	Topic findFirstByTid(Integer tid);

	@Query(
			"SELECT t, " +
					"COALESCE(CAST(SUM(CASE WHEN h.iscorrect = 1 AND h.uid = :uid THEN 1 END) * 1.0 / COUNT(DISTINCT w.wid) AS FLOAT), 0) AS progress, " +
					"COUNT(DISTINCT w.wid) AS wordCount " +
					"FROM Topic t " +
					"LEFT JOIN Word w ON t.tid = w.topic.tid " +
					"LEFT JOIN History h ON w.wid = h.word.wid AND h.uid = :uid " +
					"WHERE t.level.lid = :lid " +
					"GROUP BY t.tid"
	)
	List<Object[]> findTopicsWithProgressAndWordCountByLevelId(int lid, String uid);
}
