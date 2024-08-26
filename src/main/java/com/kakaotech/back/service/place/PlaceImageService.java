package com.kakaotech.back.service.place;

import com.kakaotech.back.common.exception.GoogleApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PlaceImageService {
    private final S3Client s3Client;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${aws.s3.bucket-name}")
    String bucketName;

    // S3에 있는 이미지 파일 가져오기
    public byte[] fetchImage(Long placeId) {
        String keyName = "trippop-image/" + placeId + ".jpg";

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
            return response.readAllBytes();
        } catch (S3Exception e) {
            logger.error("FAILED TO FETCH IMAGE {}: {}", keyName, e.getMessage());
        } catch (IOException e) {
            logger.error("FAILED TO READ IMAGE {}: {}", keyName, e.getMessage());
        }
        // 에러 발생시 null 반환
        return null;
    }

    // S3에 업로드
    public void saveImage(Long placeId, byte[] imageData) {
        String keyName = "trippop-image/" + placeId + ".jpg";
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            s3Client.putObject(request, RequestBody.fromBytes(imageData));
        } catch (S3Exception e) {
            logger.error("FAILED TO UPLOAD IMAGE: {}", e.getMessage());
        }

    }
}
