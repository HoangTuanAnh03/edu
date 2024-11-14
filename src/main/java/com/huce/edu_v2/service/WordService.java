package com.huce.edu_v2.service;

import com.huce.edu_v2.dto.response.test.TestResponse;
import com.huce.edu_v2.dto.response.word.QuestionResponse;
import com.huce.edu_v2.entity.TestHistory;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.entity.Word;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface WordService {
	Word findFirstWordBeforeWid(Integer wid, Integer tid);

	QuestionResponse getQuestion(Integer wid, Integer tid);

	Boolean checkAnswer(Integer wid, String w, String uid);

	Word findFirstByWid(Integer wid);

	Word add(Word wordEntity, Integer tid, Integer lid);

	Word edit(Word wordEntity);

	Word delete(Integer id);

	List<QuestionResponse> getTest(User user);

	List<Word> findByTid(Integer tid);

	Map<String, Object> handleCheckTest(List<TestResponse> testQuestion, String uid);

	List<TestHistory> getTestHistory(String uid);
}
