package br.com.fiap.hackaton.processor.infrastructure.message;

import br.com.fiap.hackaton.processor.core.application.GeneratedPhotosByVideoUpload;
import br.com.fiap.hackaton.processor.core.message.ConsumerMessageVideo;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerMessageVideoImpl implements ConsumerMessageVideo {

    private final GeneratedPhotosByVideoUpload generatedPhotosByVideoUpload;

    public ConsumerMessageVideoImpl(GeneratedPhotosByVideoUpload generatedPhotosByVideoUpload) {
        this.generatedPhotosByVideoUpload = generatedPhotosByVideoUpload;
    }

    @Override
    @SqsListener("${aws.queue.video-queue}")
    public void consume(String uploadId) {
        log.info("Consuming message from SQS: {}", uploadId);

        generatedPhotosByVideoUpload.execute(uploadId);

        log.info("Processing video upload with ID: {}", uploadId);
    }
}
