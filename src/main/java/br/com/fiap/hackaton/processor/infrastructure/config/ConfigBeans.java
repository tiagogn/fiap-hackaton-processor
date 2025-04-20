package br.com.fiap.hackaton.processor.infrastructure.config;

import br.com.fiap.hackaton.processor.core.application.GeneratedPhotosByVideoUpload;
import br.com.fiap.hackaton.processor.core.gateway.VideoStorageGateway;
import br.com.fiap.hackaton.processor.core.notifier.SendNotificationError;
import br.com.fiap.hackaton.processor.core.processor.video.SliceVideo;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ConfigBeans {

    private final UploadRepository uploadRepository;

    private final VideoStorageGateway videoStorageGateway;

    private final SliceVideo sliceVideo;

    private final SendNotificationError sendNotificationError;

    public ConfigBeans(UploadRepository uploadRepository, VideoStorageGateway videoStorageGateway, SliceVideo sliceVideo,
                       SendNotificationError sendNotificationError) {
        this.uploadRepository = uploadRepository;
        this.videoStorageGateway = videoStorageGateway;
        this.sliceVideo = sliceVideo;
        this.sendNotificationError = sendNotificationError;
    }

    @Bean
    public GeneratedPhotosByVideoUpload getGeneratedPhotosByVideoUpload() {
        return new GeneratedPhotosByVideoUpload(
                sliceVideo,
                uploadRepository,
                videoStorageGateway,
                sendNotificationError
        );
    }

    @Bean
    public Executor getExecutorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
