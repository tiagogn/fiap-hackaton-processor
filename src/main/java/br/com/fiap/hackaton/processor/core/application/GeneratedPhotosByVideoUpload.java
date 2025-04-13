package br.com.fiap.hackaton.processor.core.application;

import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.domain.Video;
import br.com.fiap.hackaton.processor.core.gateway.VideoStorageGateway;
import br.com.fiap.hackaton.processor.core.processor.video.SliceVideo;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import br.com.fiap.hackaton.processor.infrastructure.utils.FolderUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class GeneratedPhotosByVideoUpload {

    private final SliceVideo sliceVideo;

    private final UploadRepository uploadRepository;

    private final VideoStorageGateway videoStorageGateway;

    public GeneratedPhotosByVideoUpload(
            SliceVideo sliceVideo,
            UploadRepository uploadRepository,
            VideoStorageGateway videoStorageGateway) {
        this.sliceVideo = sliceVideo;
        this.uploadRepository = uploadRepository;
        this.videoStorageGateway = videoStorageGateway;
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

            var fileZipName = video.getId().toString()
                    .concat(".zip");

            if (StringUtils.isNotBlank(outputFolder)){
                byte[] bytes = FolderUtils.zip(outputFolder);
                videoStorageGateway.writeFile(fileZipName, bytes, upload);

                video.setZipFileName(fileZipName);
            }

            uploadRepository.save(upload);
        }
    }

}
