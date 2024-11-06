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
	final TopicService topicService;
	final UserService userService;
	final SecurityUtil securityUtil;
	final LevelService levelService;
	@GetMapping("/getByLevel")
	public ApiResponse<List<TopicResponse>> getByLevel(@RequestParam Integer lid){
		String uid = "tdf";
		User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));
		if(user != null) uid = user.getId();
		return ApiResponse.<List<TopicResponse>>builder()
				.message("Fetch all topics by levelid and user")
				.code(HttpStatus.OK.value())
				.data(topicService.findTopicsWithProgressAndWordCountByLevelId(lid, uid))
				.build();
	}

	@PostMapping("/add")
	public ApiResponse<Topic> add(@RequestParam Integer lid, @RequestParam String tname){
		if(levelService.findFirstByLid(lid) == null)
			return ApiResponse.<Topic>builder()
					.data(null)
					.code(HttpStatus.BAD_REQUEST.value())
					.message("Lid does not exists")
					.build();
		return ApiResponse.<Topic>builder()
				.data(topicService.add(lid, tname))
				.code(HttpStatus.OK.value())
				.message("Add topic successfully")
				.build();
	}

	@DeleteMapping("/delete")
	public ApiResponse<Topic> delete(@RequestParam Integer tid){
		Topic topic = topicService.delete(tid);
		ApiResponse<Topic> response = new ApiResponse<>(HttpStatus.OK.value(), "Delete Successfully", topic);

		if(topic == null){
			response.setCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage("Topic does not exists");
		}
		return response;
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
