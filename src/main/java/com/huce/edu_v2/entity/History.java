package com.huce.edu_v2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "history")
public class History {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "hid")
	private Integer id;
	@Basic
	@Column(name = "uid")
	private String uid;
	@ManyToOne
	@JoinColumn(name = "wid")
	private Word word;
	@Basic
	@Column(name = "iscorrect")
	private Integer iscorrect;

}
