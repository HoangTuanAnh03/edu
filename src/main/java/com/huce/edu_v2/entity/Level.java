package com.huce.edu_v2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "levels")
public class Level {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer lid;

	private String lname;

	private String limage;
}

