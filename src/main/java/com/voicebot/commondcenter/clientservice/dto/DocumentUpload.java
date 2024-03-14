package com.voicebot.commondcenter.clientservice.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class DocumentUpload {
    private String fileName;
    private MultipartFile file;
    private String bucketName;
}
