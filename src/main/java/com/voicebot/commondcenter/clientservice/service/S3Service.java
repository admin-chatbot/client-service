package com.voicebot.commondcenter.clientservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface S3Service {
    public void uploadFile(String bucketName, String keyName, MultipartFile file) throws IOException;
}
