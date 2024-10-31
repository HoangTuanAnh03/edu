package com.huce.edu_v2.dto.response.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponse {
    private List<String> fileName;
    private Instant uploadedAt;
}
