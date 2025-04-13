package br.com.fiap.hackaton.processor.infrastructure.gateway;

import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.gateway.VideoStorageGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class VideoStorageGatewayImpl implements VideoStorageGateway {

    private final S3Client s3Client;
    private final String bucketName;

    public VideoStorageGatewayImpl(S3Client s3Client, @Value("${aws.s3.video-bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public void readAllBytesByUpload(Upload upload) {
        log.info("Read all bytes by upload {}", upload);

        Set<String> videos = listVideos(upload.getId().toString());

        if (videos.isEmpty())
            return;

        log.info("Found {} videos for upload {}", videos.size(), upload.getId());

        upload.getVideos().forEach(video -> {
            var videoKey = videos.stream().filter( v -> v.contains(video.getName()) ).findFirst().orElse(null);
            if (videoKey != null) {
                byte[] videoBytes = loadVideo(videoKey);
                video.setInputStream(new ByteArrayInputStream(videoBytes));
                log.info("Loaded video {} with size {} bytes", video.getName(), videoBytes.length);
            } else {
                log.warn("Video {} not found in S3", video.getName());
            }
        });
        log.info("Finished loading videos for upload {}", upload.getId());
    }

    private Set<String> listVideos(String videoKey) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(videoKey)
                .build();

        var responseBytes = s3Client.listObjectsV2(request);
        return responseBytes.contents()
                .stream()
                .map(S3Object::key)
                .filter(it -> !it.endsWith("/"))
                .collect(Collectors.toSet());
    }

    private byte[] loadVideo(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);

        return objectBytes.asByteArray();
    }

    @Override
    public void writeFile(String fileName, byte[] bytes, Upload upload) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(upload.getId().toString()+"/"+fileName)
                .contentType("application/zip")
                .contentLength((long)bytes.length)
                .build();

        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        log.info("File {} uploaded to S3 with response {}", fileName, putObjectResponse);
    }
}
