package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.dto.response.topic.TopicResponse;
import com.huce.edu_v2.repository.TopicRepository;
import com.huce.edu_v2.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
	final TopicRepository topicRepository;
	@Override
	public List<TopicResponse> findTopicsWithProgressAndWordCountByLevelId(Integer lid, String uid){
		return topicRepository.findTopicsWithProgressAndWordCountByLevelId(lid, uid).stream().map(TopicResponse::new).toList();
	}

}
