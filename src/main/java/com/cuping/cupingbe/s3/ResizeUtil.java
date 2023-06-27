package com.cuping.cupingbe.s3;

import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.s3.S3Uploader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ResizeUtil {

    private final ObjectMapper objectMapper;
    private final S3Uploader s3Uploader;

    public MultipartFile resizeImage(String fileName, String fileFormatName, MultipartFile originalImage) {
        try {
            // MultipartFile -> BufferedImage Convert
            BufferedImage image = ImageIO.read(originalImage.getInputStream());
            // newWidth : newHeight = originWidth : originHeight
            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            // origin 이미지가 resizing될 사이즈보다 작을 경우 resizing 작업 안 함
            if(originWidth < 800 && originHeight < 600)
                return originalImage;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", 800);
            scale.setAttribute("newHeight", 600);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormatName, out);
            out.flush();

//            return new MockMultipartFile(fileName, fileName, fileFormatName, out);
            return new CustomMultipartFile(fileName, fileName, fileFormatName, out.toByteArray());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 리사이즈에 실패했습니다.");
        }
    }

    public ResponseEntity<Message> upload(MultipartFile multipartFile) throws IOException{
        s3Uploader.upload(resizeImage(multipartFile.getOriginalFilename()
                , multipartFile.getContentType().substring(multipartFile.getContentType().lastIndexOf("/") + 1), multipartFile));
        return new ResponseEntity<>(new Message(null), HttpStatus.OK);
    }
}
