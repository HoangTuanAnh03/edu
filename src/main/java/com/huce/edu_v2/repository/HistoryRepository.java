package com.huce.edu_v2.repository;

import com.huce.edu_v2.entity.History;
import com.huce.edu_v2.entity.Topic;
import com.huce.edu_v2.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer>, JpaSpecificationExecutor<Topic> {
	History findFirstByUidAndWord(String uid, Word word);

	@Query(value = "SELECT * FROM History WHERE DATE(datetime) >= DATE(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)) " +
			"AND DATE(datetime) <= DATE(DATE_ADD(CURDATE(), INTERVAL (6 - WEEKDAY(CURDATE())) DAY))", nativeQuery = true)
	List<History> getWeekHistory();

	@Query(value = "WITH week AS ( " +
			"SELECT DATE(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY)) AS start, " +
			"DATE(DATE_ADD(CURDATE(), INTERVAL (6 - WEEKDAY(CURDATE())) DAY)) AS end) " +
			"SELECT l.lname, COALESCE(COUNT(h.hid), 0) FROM levels l " +
			"LEFT JOIN topics t ON l.lid = t.lid " +
			"LEFT JOIN words w ON t.tid = w.tid " +
			"LEFT JOIN history h ON w.wid = h.wid " +
			"AND DATE(h.datetime) BETWEEN (SELECT start FROM week) AND (SELECT end FROM week) " +
			"GROUP BY l.lid ", nativeQuery = true)
	List<Object[]> statisticsLevel();
}
