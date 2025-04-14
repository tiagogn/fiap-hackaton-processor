package br.com.fiap.hackaton.processor.core.application;

import br.com.fiap.hackaton.processor.core.domain.StatusVideo;
import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.domain.User;
import br.com.fiap.hackaton.processor.core.domain.Video;
import br.com.fiap.hackaton.processor.core.gateway.VideoStorageGateway;
import br.com.fiap.hackaton.processor.core.notifier.SendNotificationError;
import br.com.fiap.hackaton.processor.core.processor.video.SliceVideo;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class GeneratedPhotosByVideoUploadTest {

    private SliceVideo sliceVideo;
    private UploadRepository uploadRepository;
    private VideoStorageGateway videoStorageGateway;
    private SendNotificationError sendNotificationError;
    private GeneratedPhotosByVideoUpload generatedPhotosByVideoUpload;

    @BeforeEach
    void setUp() {
        sliceVideo = mock(SliceVideo.class);
        uploadRepository = mock(UploadRepository.class);
        videoStorageGateway = mock(VideoStorageGateway.class);
        sendNotificationError = mock(SendNotificationError.class);

        generatedPhotosByVideoUpload = new GeneratedPhotosByVideoUpload(
                sliceVideo,
                uploadRepository,
                videoStorageGateway,
                sendNotificationError
        );
    }

    private Upload createUpload(UUID uploadId){
        var user = new User();
        user.setId(uploadId);
        user.setName("name");
        user.setEmail("email@email.com");
        user.setCpf("123456789");
        Upload upload = new Upload();
        upload.setId(UUID.randomUUID());
        upload.setCreationDate(LocalDateTime.now());
        upload.setUser(user);
        Video video = new Video();
        video.setId(UUID.randomUUID());
        video.setName("video 1");
        video.setSize(1024);
        video.setContentType("video/mp4");
        video.setStatus(StatusVideo.PROCESSED);
        upload.addVideo(video);
        return upload;
    }

    @Test
    @SneakyThrows
    void shouldProcessVideoAndSaveZipFile() {
        UUID uploadId = UUID.randomUUID();
        var upload = createUpload(uploadId);
        var video = upload.getVideos().get(0);
        var fileTemp = Files.createTempDirectory(video.getId().toString()) + File.separator;

        when(uploadRepository.findById(uploadId)).thenReturn(upload);
        when(sliceVideo.execute(anyString(), eq(video))).thenReturn(fileTemp);

        generatedPhotosByVideoUpload.execute(uploadId.toString());

        verify(videoStorageGateway).writeFile(anyString(), any(), eq(upload));
        verify(uploadRepository).save(upload);
    }

    @Test
    void shouldSendNotificationOnProcessingError() {
        UUID uploadId = UUID.randomUUID();
        var upload = createUpload(uploadId);
        var video = upload.getVideos().get(0);

        when(uploadRepository.findById(uploadId)).thenReturn(upload);
        when(sliceVideo.execute(anyString(), eq(video))).thenReturn(null);

        generatedPhotosByVideoUpload.execute(uploadId.toString());

        verify(sendNotificationError).sendNotification(any());
        verify(uploadRepository).save(upload);
    }

    @Test
    void shouldDoNothingWhenUploadNotFound() {
        String uploadId = UUID.randomUUID().toString();
        when(uploadRepository.findById(UUID.fromString(uploadId))).thenReturn(null);

        generatedPhotosByVideoUpload.execute(uploadId);

        verifyNoInteractions(videoStorageGateway, sendNotificationError, sliceVideo);
    }

    @Test
    @SneakyThrows
    void shouldNotProcessVideosEmpty() {
        UUID uploadId = UUID.randomUUID();
        var upload = createUpload(uploadId);
        upload.getVideos().clear();

        when(uploadRepository.findById(uploadId)).thenReturn(upload);

        generatedPhotosByVideoUpload.execute(uploadId.toString());

        verify(uploadRepository, times(1)).findById(uploadId);
        verify(videoStorageGateway, times(0)).writeFile(anyString(), any(), eq(upload));
        verify(uploadRepository, times(0)).save(upload);
    }
}