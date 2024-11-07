package com.huce.edu_v2.service.impl;

import com.huce.edu_v2.dto.response.topic.TopicResponse;
import com.huce.edu_v2.entity.Topic;
import com.huce.edu_v2.repository.LevelRepository;
import com.huce.edu_v2.repository.TopicRepository;
import com.huce.edu_v2.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
	final TopicRepository topicRepository;
	final LevelRepository levelRepository;
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
		Topic newTopic = new Topic(
				0,
				name,
				levelRepository.findFirstByLid(lid)
		);
		return topicRepository.save(newTopic);
	}

	@Override
	public Topic edit(Topic topicEntity) {
		topicRepository.save(topicEntity);
		return topicEntity;
	}

	@Override
	public Topic delete(Integer id) {
		Topic topic = topicRepository.findFirstByTid(id);
		topicRepository.delete(topic);
		return topic;
	}

	@Override
	public List<Topic> getTopicsByLid(Integer lid) {
		return topicRepository.getTopicsByLevel(levelRepository.findFirstByLid(lid));
	}
}
