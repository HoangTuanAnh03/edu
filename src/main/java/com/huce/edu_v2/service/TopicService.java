package com.huce.edu_v2.service;

import com.huce.edu_v2.dto.response.topic.TopicResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface TopicService {
	List<TopicResponse> findTopicsWithProgressAndWordCountByLevelId(Integer lid, String uid);
}
