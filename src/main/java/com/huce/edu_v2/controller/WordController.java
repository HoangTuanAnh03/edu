package com.huce.edu_v2.controller;

import com.huce.edu_v2.dto.ApiResponse;
import com.huce.edu_v2.dto.response.test.TestResponse;
import com.huce.edu_v2.dto.response.word.QuestionResponse;
import com.huce.edu_v2.entity.TestHistory;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.entity.Word;
import com.huce.edu_v2.entity.WordDict;
import com.huce.edu_v2.service.*;
import com.huce.edu_v2.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/words")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class WordController {
	SecurityUtil securityUtil;
	final WordService wordService;
	final LevelService levelService;
	final UserService userService;
	final TopicService topicService;
	final DictionaryService dictionaryService;

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
					.code(HttpStatus.BAD_REQUEST.value())
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

	@GetMapping("/getTest")
	public ApiResponse<List<QuestionResponse>> getTest(){
		User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));
		if(user == null)
			return ApiResponse.<List<QuestionResponse>>builder()
					.data(null)
					.message("Authentication error")
					.code(HttpStatus.BAD_REQUEST.value())
					.build();

		List<QuestionResponse> data = wordService.getTest(user);

		ApiResponse<List<QuestionResponse>> response = new ApiResponse<>();
		response.setData(data);
		response.setMessage("Number of words learned is less than 10");
		response.setCode(HttpStatus.OK.value());

		if(data == null) return response;
		response.setMessage("Fetch test successfully");
		return response;
	}

	@PostMapping("/add")
	public ApiResponse<Word> add(@RequestBody Word wordEntity, @RequestParam Integer topic, @RequestParam int lid) {
		Word word = wordService.add(wordEntity, topic, lid);
		ApiResponse<Word> response = new ApiResponse<>();
		response.setData(word);
		response.setCode(HttpStatus.OK.value());
		if(word == null){
			response.setCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage("Add word failed");
		}else{
			response.setMessage("Add word successfully");
		}
		return response;
	}

	@PutMapping("/edit")
	public ApiResponse<Word> edit(@RequestBody Word wordEntity){
		return ApiResponse.<Word>builder()
				.code(HttpStatus.OK.value())
				.message("Edit word successfully")
				.data(wordService.edit(wordEntity))
				.build();
	}

	@DeleteMapping("/delete")
	public ApiResponse<Word> delete(@RequestParam Integer wid){
		return ApiResponse.<Word>builder()
				.message("Delete word successfully")
				.data(wordService.delete(wid))
				.code(HttpStatus.OK.value())
				.build();
	}

	@GetMapping("/getWordsByTid")
	public ApiResponse<List<Word>> getAll(@RequestParam int tid) {
		return ApiResponse.<List<Word>>builder()
				.code(HttpStatus.OK.value())
				.message("Fetch word by tid")
				.data(wordService.findByTid(tid))
				.build();
	}

	@PostMapping("/checkTest")
	public ApiResponse<Map<String, Object>> checkTest(@RequestBody List<TestResponse> testResponses){
		User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));
		System.out.println(user);
		return ApiResponse.<Map<String, Object>>builder()
				.code(HttpStatus.OK.value())
				.message("check test question")
				.data(wordService.handleCheckTest(testResponses, user.getId()))
				.build();
	}

	@GetMapping("/getTestHistory")
	public ApiResponse<List<TestHistory>> getTestHistory(){
		User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));
		return ApiResponse.<List<TestHistory>>builder()
				.code(HttpStatus.OK.value())
				.message("fetch test history")
				.data(wordService.getTestHistory(user.getId()))
				.build();
	}

	@GetMapping("/searchWordInDict")
	public ApiResponse<List<WordDict>> searchWordInDict(@RequestParam String word){
		return ApiResponse.<List<WordDict>>builder()
				.code(HttpStatus.OK.value())
				.data(dictionaryService.searchWordInDict(word))
				.message("search word in dictionary")
				.build();
	}
}
