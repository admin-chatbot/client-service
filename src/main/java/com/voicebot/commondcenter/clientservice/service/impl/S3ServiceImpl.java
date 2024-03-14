package com.voicebot.commondcenter.clientservice.service.impl;


import com.voicebot.commondcenter.clientservice.service.S3Service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class S3ServiceImpl implements S3Service {



    @Value(value = "${amazon.s3.accessKey}")
    private String accessKey;

    @Value(value = "${amazon.s3.secretKey}")
    private String secretKey;

    @Value(value = "${amazon.s3.bucketName}")
    private String bucketName;

    @Value(value = "${amazon.s3.region}")
    private String region;

    private S3Client s3client;

    @PostConstruct
    public void S3Service() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3client = S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).region(Region.of(region)).build();
    }

    @Override
    public PutObjectResponse uploadFile( MultipartFile file) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(generateFileName(file))
                .build();
        return s3client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    @Override
    public PutObjectResponse uploadFile(MultipartFile file, String bucket) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(generateFileName(file))
                .build();
        return s3client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}
