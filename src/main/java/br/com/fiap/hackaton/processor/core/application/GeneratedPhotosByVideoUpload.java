package br.com.fiap.hackaton.processor.core.application;

import br.com.fiap.hackaton.processor.core.domain.Notification;
import br.com.fiap.hackaton.processor.core.domain.StatusVideo;
import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.domain.Video;
import br.com.fiap.hackaton.processor.core.gateway.VideoStorageGateway;
import br.com.fiap.hackaton.processor.core.notifier.SendNotificationError;
import br.com.fiap.hackaton.processor.core.processor.video.SliceVideo;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import br.com.fiap.hackaton.processor.infrastructure.utils.FolderUtils;
import br.com.fiap.hackaton.processor.infrastructure.utils.FormatDateUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class GeneratedPhotosByVideoUpload {

    private final SliceVideo sliceVideo;

    private final UploadRepository uploadRepository;

    private final VideoStorageGateway videoStorageGateway;

    private final SendNotificationError sendNotificationError;

    public GeneratedPhotosByVideoUpload(
            SliceVideo sliceVideo,
            UploadRepository uploadRepository,
            VideoStorageGateway videoStorageGateway,
            SendNotificationError sendNotificationError) {
        this.sliceVideo = sliceVideo;
        this.uploadRepository = uploadRepository;
        this.videoStorageGateway = videoStorageGateway;
        this.sendNotificationError = sendNotificationError;
    }

    public void execute(String uploadId){

        Upload upload = uploadRepository.findById(UUID.fromString(uploadId));

        if (upload == null)
            return;

        videoStorageGateway.readAllBytesByUpload(upload);

        if (upload.getVideos().isEmpty())
            return;

        for (Video video : upload.getVideos()) {
            var outputFolder = sliceVideo.execute(upload.getUser().getCpf(), video);

            if (StringUtils.isNotBlank(outputFolder) && video.getStatus() == StatusVideo.PROCESSED ){
                var fileZipName = video.getId().toString()
                        .concat(".zip");

                byte[] bytes = FolderUtils.zip(outputFolder);
                videoStorageGateway.writeFile(fileZipName, bytes, upload);
                video.setZipFileName(fileZipName);
            }
            else{
                Notification notification = Notification.builder()
                        .to(upload.getUser().getCpf())
                        .subject("""
                                    Erro ao processar video do upload feito em %s por %s
                                 """.formatted(FormatDateUtils.format(upload.getCreationDate()), upload.getUser().getName()))
                        .body("""
                              Caro(a) %s,
                              O video %s não pode ser processado em %s, devido a problemas encontrados no processamento.
                              Por favor, tente novamente.
                              
                              Atenciosamente,
                              """.formatted(upload.getUser().getName(), video.getName(), FormatDateUtils.format(LocalDateTime.now())))
                        .build();

                sendNotificationError.sendNotification(notification);
            }

            uploadRepository.save(upload);
        }
    }

}
