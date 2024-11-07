package com.huce.edu_v2.controller;

import com.huce.edu_v2.dto.ApiResponse;
import com.huce.edu_v2.dto.response.level.LevelResponse;
import com.huce.edu_v2.entity.User;
import com.huce.edu_v2.service.LevelService;
import com.huce.edu_v2.service.UserService;
import com.huce.edu_v2.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/levels")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class LevelController {
	LevelService levelService;
	UserService userService;
	SecurityUtil securityUtil;

	@GetMapping("/getAll")
	public ApiResponse<List<LevelResponse>> getAll(){
		User user = userService.fetchUserByEmail(securityUtil.getCurrentUserLogin().orElse(null));

		return ApiResponse.<List<LevelResponse>>builder()
				.code(HttpStatus.OK.value())
				.message("Fetch all levels by user")
				.data(levelService.findAllLevelsWithProgressForUser(user.getId()))
				.build();
	}
}
