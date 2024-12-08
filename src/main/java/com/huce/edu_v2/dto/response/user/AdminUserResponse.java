package com.huce.edu_v2.dto.response.user;


import com.huce.edu_v2.util.constant.GenderEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUserResponse {
	String id;
	String email;
	String name;
	String role;
	GenderEnum gender;
	String address;
	LocalDate dob;
	String mobileNumber;
	Boolean noPassword;
	String image;
	LocalDateTime createdAt;
}
