package com.huce.edu_v2.controller;

import com.huce.edu_v2.dto.ApiResponse;
import com.huce.edu_v2.dto.response.topic.TopicResponse;
import com.huce.edu_v2.entity.Topic;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.service.LevelService;
import com.huce.edu_v2.service.TopicService;
import com.huce.edu_v2.service.UserService;
import com.huce.edu_v2.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class TopicController {
	TopicService topicService;
	UserService userService;
	SecurityUtil securityUtil;
	LevelService levelService;

	@GetMapping("/getByLevel")
	public ApiResponse<List<TopicResponse>> getByLevel(@RequestParam Integer lid){
		User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));

		return ApiResponse.<List<TopicResponse>>builder()
				.message("Fetch all topics by levelId and user")
				.code(HttpStatus.OK.value())
				.data(topicService.findTopicsWithProgressAndWordCountByLevelId(lid, user.getId()))
				.build();
	}

	@PostMapping("/add")
	public ApiResponse<Topic> add(@RequestParam Integer lid, @RequestParam String tname){
		levelService.findFirstByLid(lid);
		return ApiResponse.<Topic>builder()
				.data(topicService.add(lid, tname))
				.code(HttpStatus.OK.value())
				.message("Add topic successfully")
				.build();
	}

	@DeleteMapping("/delete")
	public ApiResponse<Topic> delete(@RequestParam Integer tid){
		return ApiResponse.<Topic>builder()
				.data(topicService.delete(tid))
				.code(HttpStatus.OK.value())
				.message("Delete topic successfully")
				.build();
	}

	@GetMapping("/getTopicsByLid")
	public ApiResponse<List<Topic>> getTopicsByLid(@RequestParam Integer lid){
		return ApiResponse.<List<Topic>>builder()
				.data(topicService.getTopicsByLid(lid))
				.code(HttpStatus.OK.value())
				.message("Fetch topic successfully")
				.build();
	}
}
