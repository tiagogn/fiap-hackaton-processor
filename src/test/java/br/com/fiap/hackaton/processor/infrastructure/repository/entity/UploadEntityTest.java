package br.com.fiap.hackaton.processor.infrastructure.repository.entity;

import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.domain.User;
import br.com.fiap.hackaton.processor.core.domain.Video;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UploadEntityTest {

    @Test
    void shouldConvertFromDomainToEntity() {
        // Criação do objeto de domínio
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");

        Video video = new Video();
        video.setId(UUID.randomUUID());
        video.setName("Test Video");

        Upload upload = new Upload();
        upload.setId(UUID.randomUUID());
        upload.setCreationDate(LocalDateTime.now());
        upload.setUser(user);
        upload.addVideo(video);

        // Conversão para entidade
        UploadEntity uploadEntity = UploadEntity.fromDomain(upload);

        // Validações
        assertEquals(upload.getId(), uploadEntity.getId());
        assertEquals(upload.getCreationDate(), uploadEntity.getCreationDate());
        assertEquals(upload.getUser().getId(), uploadEntity.getUserEntity().getId());
        assertEquals(1, uploadEntity.getVideosEntity().size());
        assertEquals(upload.getVideos().get(0).getId(), uploadEntity.getVideosEntity().get(0).getId());
    }

    @Test
    void shouldConvertFromEntityToDomain() {
        // Criação da entidade
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setName("Test User");

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setId(UUID.randomUUID());
        videoEntity.setName("Test Video");

        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setId(UUID.randomUUID());
        uploadEntity.setCreationDate(LocalDateTime.now());
        uploadEntity.setUserEntity(userEntity);
        uploadEntity.addVideoEntity(videoEntity);

        // Conversão para domínio
        Upload upload = uploadEntity.toDomain();

        // Validações
        assertEquals(uploadEntity.getId(), upload.getId());
        assertEquals(uploadEntity.getCreationDate(), upload.getCreationDate());
        assertEquals(uploadEntity.getUserEntity().getId(), upload.getUser().getId());
        assertEquals(1, upload.getVideos().size());
        assertEquals(uploadEntity.getVideosEntity().get(0).getId(), upload.getVideos().get(0).getId());
    }

    @Test
    void shouldAddVideoEntity() {
        // Criação da entidade
        UploadEntity uploadEntity = new UploadEntity();
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setId(UUID.randomUUID());
        videoEntity.setName("Test Video");

        // Adiciona o vídeo à entidade
        uploadEntity.addVideoEntity(videoEntity);

        // Validações
        assertEquals(1, uploadEntity.getVideosEntity().size());
        assertEquals(uploadEntity, videoEntity.getUploadEntity());
    }
}