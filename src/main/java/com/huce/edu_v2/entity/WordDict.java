package com.huce.edu_v2.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WordDict {
	private String word;
	private String type;
	private String mean;
	private String pronun;
}
