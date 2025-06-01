package com.honeystone.common.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.extern.slf4j.Slf4j;

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
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            String formattedFileName = String.format(
                "%s_%d_%s%s", prefix, id, timestamp, extension
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