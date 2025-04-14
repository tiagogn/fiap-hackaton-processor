package br.com.fiap.hackaton.processor.infrastructure.gateway;

import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.domain.Video;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class VideoStorageGatewayImplTest {

    @Test
    void shouldStoreVideoSuccessfully() {
        S3Client s3Client = mock(S3Client.class);

        VideoStorageGatewayImpl videoStorageGateway = new VideoStorageGatewayImpl(s3Client, "videos");

        Upload upload = new Upload();
        upload.setId(UUID.randomUUID());

        Video video = new Video();
        video.setId(UUID.randomUUID());
        video.setName("Test Video");
        video.setInputStream(new ByteArrayInputStream(UUID.randomUUID().toString().getBytes()));
        upload.addVideo(video);

        videoStorageGateway.writeFile(video.getName(), video.getInputStream().readAllBytes(), upload);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void shouldGetVideoSuccessfully() {
        Upload upload = new Upload();
        upload.setId(UUID.randomUUID());
        Video video1 = new Video();
        video1.setId(UUID.randomUUID());
        video1.setName("video1.mp4");
        Video video2 = new Video();
        video2.setId(UUID.randomUUID());
        video2.setName("video2.mp4");
        upload.addVideo(video1);
        upload.addVideo(video2);

        S3Client s3Client = mock(S3Client.class);
        S3Object file1 = S3Object.builder().key(upload.getId().toString()+"/video1.mp4").build();
        S3Object file2 = S3Object.builder().key(upload.getId().toString()+"/video2.mp4").build();

        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(Arrays.asList(file1, file2))
                .build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        byte[] mockBytes = "conteúdo do arquivo".getBytes();
        ResponseBytes<GetObjectResponse> mockResponseBytes = ResponseBytes.fromByteArray(mock(GetObjectResponse.class), mockBytes);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(mockResponseBytes);

        VideoStorageGatewayImpl videoStorageGateway = new VideoStorageGatewayImpl(s3Client, "videos");
        videoStorageGateway.readAllBytesByUpload(upload);

        verify(s3Client, times(1)).listObjectsV2(any(ListObjectsV2Request.class));
        assertNotNull(video1.getInputStream());
        assertNotNull(video2.getInputStream());
    }

    @Test
    void shouldNotFindVideoInStorage() {
        Upload upload = new Upload();
        upload.setId(UUID.randomUUID());
        Video video1 = new Video();
        video1.setId(UUID.randomUUID());
        video1.setName("video1.mp4");
        Video video2 = new Video();
        video2.setId(UUID.randomUUID());
        video2.setName("video2.mp4");
        upload.addVideo(video1);
        upload.addVideo(video2);

        S3Client s3Client = mock(S3Client.class);
        S3Object file1 = S3Object.builder().key(upload.getId().toString()+"/videoX.mp4").build();
        S3Object file2 = S3Object.builder().key(upload.getId().toString()+"/videoY.mp4").build();

        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(Arrays.asList(file1, file2))
                .build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        byte[] mockBytes = "conteúdo do arquivo".getBytes();
        ResponseBytes<GetObjectResponse> mockResponseBytes = ResponseBytes.fromByteArray(mock(GetObjectResponse.class), mockBytes);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(mockResponseBytes);

        VideoStorageGatewayImpl videoStorageGateway = new VideoStorageGatewayImpl(s3Client, "videos");
        videoStorageGateway.readAllBytesByUpload(upload);

        verify(s3Client, times(1)).listObjectsV2(any(ListObjectsV2Request.class));
        assertNull(video1.getInputStream());
        assertNull(video2.getInputStream());
    }

    @Test
    void shouldGetVideoEmptySuccessfully() {
        Upload upload = new Upload();
        upload.setId(UUID.randomUUID());
        Video video1 = new Video();
        video1.setId(UUID.randomUUID());
        video1.setName("video1.mp4");
        Video video2 = new Video();
        video2.setId(UUID.randomUUID());
        video2.setName("video2.mp4");
        upload.addVideo(video1);
        upload.addVideo(video2);

        S3Client s3Client = mock(S3Client.class);

        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(List.of())
                .build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        byte[] mockBytes = "conteúdo do arquivo".getBytes();
        ResponseBytes<GetObjectResponse> mockResponseBytes = ResponseBytes.fromByteArray(mock(GetObjectResponse.class), mockBytes);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(mockResponseBytes);

        VideoStorageGatewayImpl videoStorageGateway = new VideoStorageGatewayImpl(s3Client, "videos");
        videoStorageGateway.readAllBytesByUpload(upload);

        verify(s3Client, times(1)).listObjectsV2(any(ListObjectsV2Request.class));
    }
}