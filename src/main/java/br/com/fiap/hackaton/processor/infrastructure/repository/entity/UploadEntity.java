package br.com.fiap.hackaton.processor.infrastructure.repository.entity;

import br.com.fiap.hackaton.processor.core.domain.Upload;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "upload")
@Data
@NoArgsConstructor
public class UploadEntity {
    @Id
    private UUID id;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "uploadEntity")
    private List<VideoEntity> videosEntity = new ArrayList<>();
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity userEntity;
    private LocalDateTime creationDate;

    public void addVideoEntity(VideoEntity videoEntity) {
        videosEntity.add(videoEntity);
        videoEntity.setUploadEntity(this);
    }

    public Upload toDomain(){
        var upload = new Upload();
        upload.setId(id);
        upload.setCreationDate(creationDate);
        upload.setUser(userEntity.toDomain());
        videosEntity.forEach(videoEntity -> upload.addVideo(videoEntity.toDomain()));
        return upload;
    }

    public static UploadEntity fromDomain(Upload upload){
        var uploadEntity = new UploadEntity();
        uploadEntity.setId(upload.getId());
        uploadEntity.setCreationDate(upload.getCreationDate());
        uploadEntity.setUserEntity(UserEntity.fromDomain(upload.getUser()));
        upload.getVideos().forEach(video -> uploadEntity.addVideoEntity(VideoEntity.fromDomain(video)));
        return uploadEntity;
    }
}
