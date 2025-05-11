package com.honeystone.common.util;

import com.amazonaws.services.s3.AmazonS3;
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
    private final FileUpload fileUpload;

    public FileRemove(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    @Autowired
    private AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final String RV_DIR = "file/video/";


    public void removeFile(Long id) {
        final String file = String.format("videos_%d", id);
        ObjectListing objectListing = amazonS3Client.listObjects(bucket, RV_DIR);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            String filename = os.getKey();
            log.info("filename: {}", filename);
            if (filename.startsWith(RV_DIR + file)) {
                amazonS3Client.deleteObject(bucket, filename);
            }
        }
    }
}