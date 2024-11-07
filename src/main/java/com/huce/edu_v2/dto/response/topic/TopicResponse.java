package com.huce.edu_v2.dto.response.topic;

import com.huce.edu_v2.entity.Topic;

public class TopicResponse {
	Integer tid;
	String tname;
	long numWords;
	float progress;

	public TopicResponse(Object[] res){
		Topic topic = (Topic) res[0];
		this.tid = topic.getTid();
		this.tname = topic.getTname();
		this.progress = (float) res[1];
		this.numWords = (int) res[2];
	}
}
