package com.huce.edu_v2.controller;

import com.huce.edu_v2.dto.ApiResponse;
import com.huce.edu_v2.dto.response.topic.TopicResponse;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.service.TopicService;
import com.huce.edu_v2.service.UserService;
import com.huce.edu_v2.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topics")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class TopicController {
	final TopicService topicService;
	final UserService userService;
	final SecurityUtil securityUtil;
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
}
