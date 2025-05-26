package com.honeystone.common.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FileUpload {
    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String generateFileName(String prefix, Long id, MultipartFile file) {


            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ""; // 기본값을 빈 문자열로 설정

            String formattedFileName = String.format(
                "%s_%d%s", prefix, id, extension
            );

        return formattedFileName;
    }

    public String uploadFile(MultipartFile file, String fileName, String type) throws IOException {

        String fileUrl = "file/" + type + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileUrl, file.getInputStream(), metadata);

        return fileUrl;
    }
}