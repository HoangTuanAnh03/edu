package com.huce.edu_v2.service;

import com.huce.edu_v2.dto.response.topic.TopicResponse;
import com.huce.edu_v2.entity.Topic;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TopicService {
	List<TopicResponse> findTopicsWithProgressAndWordCountByLevelId(Integer lid, String uid);

	Topic findFirstByTid(Integer tid);

	Topic add(Integer lid, String name);

	Topic edit(Topic topicEntity);

	Topic delete(Integer tid);

	List<Topic> getTopicsByLid(Integer lid);
}
