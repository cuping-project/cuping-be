package com.cuping.cupingbe.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {
        String fileName = "cuping-image/" + UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getSize());
        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objMeta);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void delete(String fileUrl) {
        try {
            String url = fileUrl.substring(50);
            String[] temp = url.split("_");
            String fileKey = temp[0] + "_" + URLDecoder.decode(temp[1], StandardCharsets.UTF_8);
            amazonS3.deleteObject(bucket, fileKey);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.IMAGE_DELETE_FAIL);
        }
    }
}
