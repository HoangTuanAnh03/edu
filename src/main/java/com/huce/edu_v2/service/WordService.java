package com.huce.edu_v2.service;

import com.huce.edu_v2.dto.response.word.QuestionResponse;
import com.huce.edu_v2.entity.Word;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface WordService {
	 List<Word> findAll();
	 Word findFirstWordBeforeWid(Integer wid, Integer tid);

//	public QuestionResponse getQuestion(Integer wid, Integer tid);
	 QuestionResponse getQuestion(Integer wid, Integer tid);

	 Boolean checkAnswer(Integer wid, String w, String uid);

	 Word findFirstByWid(Integer wid);
}
