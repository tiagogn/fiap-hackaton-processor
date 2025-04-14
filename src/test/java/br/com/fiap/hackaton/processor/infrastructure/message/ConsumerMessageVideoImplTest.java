package br.com.fiap.hackaton.processor.infrastructure.message;

import br.com.fiap.hackaton.processor.core.application.GeneratedPhotosByVideoUpload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.mockito.Mockito.*;

class ConsumerMessageVideoImplTest {

    @Value("${aws.queue.video-queue}")
    private String queueName;

    @Test
    void shouldConsumeMessageAndExecuteGeneratedPhotosByVideoUpload() {
        // Mock da dependência
        GeneratedPhotosByVideoUpload generatedPhotosByVideoUpload = mock(GeneratedPhotosByVideoUpload.class);

        // Instância da classe a ser testada
        ConsumerMessageVideoImpl consumer = new ConsumerMessageVideoImpl(generatedPhotosByVideoUpload);

        // ID de upload simulado
        String uploadId = "12345";

        // Execução do método consume
        consumer.consume(uploadId);

        // Verificação se o método execute foi chamado com o ID correto
        verify(generatedPhotosByVideoUpload, times(1)).execute(uploadId);
    }
}