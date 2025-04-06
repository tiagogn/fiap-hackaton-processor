package br.com.fiap.hackaton.processor.infrastructure.repository.entity;

import br.com.fiap.hackaton.processor.core.domain.StatusVideo;
import br.com.fiap.hackaton.processor.core.domain.Video;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.UUID;

@Entity
@Table(name = "video")
@Data
@NoArgsConstructor
public class VideoEntity {
    @Id
    private UUID id;
    private String name;
    private Integer size;
    private String contentType;
    private String uri;
    @JoinColumn(name = "upload_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UploadEntity uploadEntity;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private StatusVideo status;

    public Video toDomain(){
        var video = new Video();
        video.setId(id);
        video.setName(name);
        video.setSize(size);
        video.setContentType(contentType);
        video.setUri(uri);
        video.setStatus(status);
        return video;
    }

    public static VideoEntity fromDomain(Video video){
        var videoEntity = new VideoEntity();
        videoEntity.setId(video.getId());
        videoEntity.setName(video.getName());
        videoEntity.setSize(video.getSize());
        videoEntity.setContentType(video.getContentType());
        videoEntity.setUri(video.getUri());
        videoEntity.setStatus(video.getStatus());
        return videoEntity;
    }
}
