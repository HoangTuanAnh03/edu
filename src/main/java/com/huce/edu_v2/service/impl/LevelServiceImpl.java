package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.dto.response.level.LevelResponse;
import com.huce.edu_v2.entity.Level;
import com.huce.edu_v2.repository.LevelRepository;
import com.huce.edu_v2.service.LevelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class LevelServiceImpl implements LevelService {


	final LevelRepository levelRepository;

	@Override
	public List<Level> getAll(){
		return levelRepository.findAll();
	}

	@Override
	public Level findFirstByLid(Integer lid){
		return levelRepository.findFirstByLid(lid);
	}
	@Override
	public List<LevelResponse> findAllLevelsWithProgressForUser(String uid){
		return levelRepository.findAllLevelsWithProgressForUser(uid).stream().map(LevelResponse::new).toList();
	}


}
