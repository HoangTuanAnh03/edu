package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.advice.exception.ResourceNotFoundException;
import com.huce.edu_v2.dto.response.level.LevelResponse;
import com.huce.edu_v2.entity.Level;
import com.huce.edu_v2.repository.LevelRepository;
import com.huce.edu_v2.service.LevelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LevelServiceImpl implements LevelService {
	LevelRepository levelRepository;

	@Override
	public Level findFirstByLid(Integer lid){
		return levelRepository.findById(lid).orElseThrow(
				() -> new ResourceNotFoundException("Level", "id", lid)
		);
	}

	@Override
	public List<LevelResponse> findAllLevelsWithProgressForUser(String uid){
		return levelRepository.findAllLevelsWithProgressForUser(uid).stream().map(LevelResponse::new).toList();
	}
}
