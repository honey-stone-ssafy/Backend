package com.honeystone.common.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FileRemove {

    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final String RV_DIR = "file/deleted/";

    public void removeUserProfileFile(String fileUrl) {
        if (amazonS3Client.doesObjectExist(bucket, fileUrl)) {
            amazonS3Client.deleteObject(bucket, fileUrl);
            log.info("프로필 이미지 {} 를 삭제했습니다.", fileUrl);
        } else {
            log.warn("삭제 시도한 파일 {} 이 존재하지 않습니다.", fileUrl);
        }
    }

    public void removeFile(Long id) {
        final String file = String.format("board_%d", id);
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, RV_DIR);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            String filename = os.getKey();
            log.info("filename: {}", filename);
            if (filename.startsWith(RV_DIR + file)) {
                amazonS3Client.deleteObject(bucket, filename);
                break;
            }
        }
    }

    public void moveFile(String fileUrl, String filename){
        // 1. 복사
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, fileUrl, bucket, RV_DIR+filename);
        amazonS3Client.copyObject(copyObjectRequest);

        // 2. 원본 삭제
        amazonS3Client.deleteObject(bucket, fileUrl);

    }
}