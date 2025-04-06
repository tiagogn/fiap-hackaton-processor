package br.com.fiap.hackaton.processor.core.application;

import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.gateway.VideoStorageGateway;
import br.com.fiap.hackaton.processor.core.processor.video.SliceVideo;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import br.com.fiap.hackaton.processor.infrastructure.utils.FolderUtils;

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

        var outputFolder = upload.getVideos()
                .stream()
                .map( path -> sliceVideo.execute(upload.getUser().getCpf(), path))
                .distinct()
                .findFirst();

        var fileZipName = upload.getUser().getCpf().concat("_")
                .concat(upload.getId().toString())
                .concat(".zip");

        //outputFolder.ifPresent(s -> FolderUtils.zip(s, fileZipName));

        if (outputFolder.isPresent()){

            byte[] bytes = FolderUtils.zip(outputFolder.get());

            videoStorageGateway.writeFile(fileZipName, bytes, upload);

            upload.setZipFile(fileZipName);
        }

    }

}
