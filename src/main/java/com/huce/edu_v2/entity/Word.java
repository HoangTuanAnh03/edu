package com.huce.edu_v2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "words")
public class Word {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer wid;

	private String word;
	private String pronun;
	private String entype;
	private String vietype;
	private String voice;
	private String photo;
	private String meaning;
	private String endesc;
	private String viedesc;

	@ManyToOne
	@JoinColumn(name = "tid")
	private Topic topic;
}
