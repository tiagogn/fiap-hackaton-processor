package br.com.fiap.hackaton.processor.infrastructure.config;

import br.com.fiap.hackaton.processor.core.application.GeneratedPhotosByVideoUpload;
import br.com.fiap.hackaton.processor.core.gateway.VideoStorageGateway;
import br.com.fiap.hackaton.processor.core.processor.video.SliceVideo;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {

    private final UploadRepository uploadRepository;

    private final VideoStorageGateway videoStorageGateway;

    private final SliceVideo sliceVideo;

    public ConfigBeans(UploadRepository uploadRepository, VideoStorageGateway videoStorageGateway, SliceVideo sliceVideo) {
        this.uploadRepository = uploadRepository;
        this.videoStorageGateway = videoStorageGateway;
        this.sliceVideo = sliceVideo;
    }

    @Bean
    public GeneratedPhotosByVideoUpload getGeneratedPhotosByVideoUpload() {
        return new GeneratedPhotosByVideoUpload(
                sliceVideo,
                uploadRepository,
                videoStorageGateway
        );
    }
}
