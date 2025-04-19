package br.com.fiap.hackaton.processor.infrastructure.repository;

import br.com.fiap.hackaton.processor.core.domain.StatusVideo;
import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.domain.Video;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import br.com.fiap.hackaton.processor.infrastructure.repository.entity.UserEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import(UploadRepositoryPostgres.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UploadRepositoryPostgresTest {

    static PostgreSQLContainer<?> postgreSQLContainer;

    @BeforeAll
    static void setUp() {
        postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
        postgreSQLContainer.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.close();
        postgreSQLContainer.stop();
    }

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity createUserEntity(){
        var userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setEmail("email@email.com");
        userEntity.setName("name");
        userEntity.setCpf("12345678901");
        return userEntity;
    }

    private Upload createUpload(){
        var userEntity = createUserEntity();
        userRepository.save(userEntity);
        Upload upload = new Upload();
        upload.setId(UUID.randomUUID());
        upload.setCreationDate(LocalDateTime.now());
        upload.setUser(userEntity.toDomain());
        Video video = new Video();
        video.setId(UUID.randomUUID());
        video.setName("video 1");
        video.setSize(1024);
        video.setContentType("video/mp4");
        video.setStatus(StatusVideo.PENDING);
        upload.addVideo(video);
        return upload;
    }

    @Test
    void shouldSaveAndFindUploadById() {

        var upload = createUpload();
        uploadRepository.save(upload);

        Upload foundUpload = uploadRepository.findById(upload.getId());

        assertNotNull(foundUpload);
        assertEquals(upload.getId(), foundUpload.getId());
        assertEquals(upload.getCreationDate(), foundUpload.getCreationDate());
        assertEquals(upload.getUser().getId(), foundUpload.getUser().getId());
        assertEquals(1, foundUpload.getVideos().size());
    }

    @Test
    void shouldSaveAndUpdateVideo(){
        var upload = createUpload();
        uploadRepository.save(upload);
        upload.getVideos().forEach(video -> video.setStatus(StatusVideo.PROCESSED));
        uploadRepository.save(upload);
        Upload foundUpload = uploadRepository.findById(upload.getId());

        assertNotNull(foundUpload);
        assertEquals(upload.getId(), foundUpload.getId());
        assertEquals(upload.getCreationDate(), foundUpload.getCreationDate());
        assertEquals(upload.getUser().getId(), foundUpload.getUser().getId());
        assertEquals(1, foundUpload.getVideos().size());
        assertEquals(StatusVideo.PROCESSED, foundUpload.getVideos().get(0).getStatus());
    }
}