package com.voicebot.commondcenter.clientservice.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.voicebot.commondcenter.clientservice.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3ServiceImpl implements S3Service {
    private AmazonS3 s3client;

    public void S3Service() {
        this.s3client = AmazonS3ClientBuilder.defaultClient();
    }

    @Override
    public void uploadFile(String bucketName, String keyName, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        s3client.putObject(bucketName, keyName, file.getInputStream(), metadata);
    }
}
