package com.huce.edu_v2.controller;

import com.huce.edu_v2.dto.ApiResponse;
import com.huce.edu_v2.dto.response.word.QuestionResponse;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.entity.Word;
import com.huce.edu_v2.service.LevelService;
import com.huce.edu_v2.service.UserService;
import com.huce.edu_v2.service.WordService;
import com.huce.edu_v2.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/words")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class WordController {
	SecurityUtil securityUtil;
	final WordService wordService;
	final LevelService levelService;
	final UserService userService;

	@GetMapping("/getQuestion")
	public ApiResponse<QuestionResponse> getQuestion(@RequestParam(defaultValue = "0") Integer wid, @RequestParam Integer tid){
		return ApiResponse.<QuestionResponse>builder()
				.code(HttpStatus.OK.value())
				.message("Fetch question")
				.data(wordService.getQuestion(wid, tid))
				.build();
	}

	@GetMapping("/checkAnswer")
	public ApiResponse<Word> checkAnswer(@RequestParam Integer wid, @RequestParam String word){
		User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));
		Boolean res = null;
		if(user != null)
			res = wordService.checkAnswer(wid, word, user.getId());
		if(res == null)
			return ApiResponse.<Word>builder()
					.code(HttpStatus.OK.value())
					.message("Authentication error")
					.data(null)
					.build();


		ApiResponse<Word> response = new ApiResponse<>();
		response.setCode(HttpStatus.OK.value());
		response.setData(wordService.findFirstByWid(wid));

		if(res){
			response.setMessage("Correct answer");
		}else{
			response.setMessage("Incorrect answer");
		}
		return response;
	}

}
