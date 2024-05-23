package com.hzboiler.module.base.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@Profile(value = {"prod"})
public class MinioStorageService extends AbstractStorageService {

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "minio")
    public static class MinioConfig {

        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
    }

    private MinioClient minioClient;
    private String bucketName;

    @Autowired
    public void setMinioClient(MinioConfig minioConfig) throws Exception {
        minioClient =
                MinioClient.builder()
                        .endpoint(minioConfig.getEndpoint())
                        .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                        .build();

        bucketName = minioConfig.getBucketName();
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            throw new RuntimeException("Initialize MinioStorageService failed. Bucket '" + bucketName + "' doesn't exist!");
        }
    }

    @Override
    public void init() throws Exception {
        log.info("MinioStorageService initialized.");
    }

    @Override
    public String store(MultipartFile multipartFile) throws Exception {
        String filename = getFilename(multipartFile);
        String contentType = multipartFile.getContentType();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
        };
        return filename;
    }

    @Override
    public Resource loadAsResource(String filename) throws Exception {
        return new InputStreamResource(minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filename)
                        .build()));
    }
}
