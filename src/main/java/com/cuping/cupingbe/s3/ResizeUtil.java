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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ResizeUtil {

    private final S3Uploader s3Uploader;

    public MultipartFile resizeImage(MultipartFile multipartFile) {
        try {
            String fileFormat = multipartFile.getOriginalFilename().substring(
                    multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            // MultipartFile -> BufferedImage Convert
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            // newWidth : newHeight = originWidth : originHeight
            int newWidth = (int) (image.getWidth() * 0.6d);
            int newHeight = (int) (image.getHeight() * 0.6d);

            if (newWidth < 600 && newHeight < 400)
                return multipartFile;

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", newWidth);
            scale.setAttribute("newHeight", newHeight);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormat, out);
            out.flush();

            return new CustomMultipartFile(multipartFile.getName(), multipartFile.getOriginalFilename(),
                    fileFormat, out.toByteArray());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 리사이즈에 실패했습니다.");
        }
    }
}
