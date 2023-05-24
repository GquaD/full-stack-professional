package com.amigoscode.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.internal.crt.DefaultS3CrtAsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void putObject(String bucketName, String key, byte[] file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromBytes(file));
    }

    public byte[] getObject(String bucketName, String keyName) {
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        ResponseInputStream<GetObjectResponse> res = s3Client.getObject(objectRequest);
        try {
            return res.readAllBytes();
        } catch (IOException e) {
            System.out.println(e.getCause());
            throw new RuntimeException(e);
        }
    }
}
