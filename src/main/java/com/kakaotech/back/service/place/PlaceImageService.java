package com.kakaotech.back.service.place;

import com.kakaotech.back.common.exception.GoogleApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PlaceImageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // TODO: DB에 저장
    public void saveImage(Long placeId, byte[] imageData) {

        Path path = Paths.get("src", "main", "resources", "image", placeId + ".jpg");
        try {
            Files.write(path, imageData);
        } catch (IOException e) {
            logger.error("FAILED TO SAVE IMAGE: {}", e.getMessage());
            throw new GoogleApiException("구글 이미지 저장 실패");
        }
    }
}
