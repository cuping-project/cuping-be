package com.cuping.cupingbe.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
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

    public boolean delete(String fileUrl) {
        try {
            String[] temp = fileUrl.split("/");
            String[] temp2 = temp[temp.length-1].split("_");
            String decodedString = URLDecoder.decode(temp2[temp2.length-1] , "UTF-8");
            String fileKey = "cuping-image/" + temp2[temp2.length-2] + "_" + decodedString;

            amazonS3.deleteObject(bucket, fileKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
