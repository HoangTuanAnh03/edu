package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.advice.exception.ResourceNotFoundException;
import com.huce.edu_v2.dto.response.topic.TopicResponse;
import com.huce.edu_v2.entity.Topic;
import com.huce.edu_v2.repository.LevelRepository;
import com.huce.edu_v2.repository.TopicRepository;
import com.huce.edu_v2.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicServiceImpl implements TopicService {
	TopicRepository topicRepository;
	LevelRepository levelRepository;

	@Override
	public List<TopicResponse> findTopicsWithProgressAndWordCountByLevelId(Integer lid, String uid){
		return topicRepository.findTopicsWithProgressAndWordCountByLevelId(lid, uid).stream().map(TopicResponse::new).toList();
	}

	@Override
	public Topic findFirstByTid(Integer tid) {
		return topicRepository.findFirstByTid(tid);
	}

	@Override
	public Topic add(Integer lid, String name) {
		return topicRepository.save(Topic.builder()
				.tname(name)
				.level(levelRepository.findFirstByLid(lid))
				.build());
	}

	@Override
	public Topic edit(Topic topicEntity) {
		return topicRepository.save(topicEntity);
	}

	@Override
	public Topic delete(Integer id) {
		Topic topic = topicRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Topic", "id", id)
		);
		topicRepository.delete(topic);
		return topic;
	}

	@Override
	public List<Topic> getTopicsByLid(Integer lid) {
		return topicRepository.getTopicsByLevel(levelRepository.findFirstByLid(lid));
	}
}
