package com.huce.edu_v2.service;

import com.huce.edu_v2.dto.response.level.LevelResponse;
import com.huce.edu_v2.entity.Level;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface LevelService {
	 List<Level> getAll();
	 Level findFirstByLid(Integer lid);

	 List<LevelResponse> findAllLevelsWithProgressForUser(String uid);
}
