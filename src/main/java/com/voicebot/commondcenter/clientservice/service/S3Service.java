package com.voicebot.commondcenter.clientservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

@Service
public interface S3Service {
    public PutObjectResponse uploadFile( MultipartFile file) throws IOException;

    public PutObjectResponse uploadFile( MultipartFile file ,String bucket) throws IOException;
}
