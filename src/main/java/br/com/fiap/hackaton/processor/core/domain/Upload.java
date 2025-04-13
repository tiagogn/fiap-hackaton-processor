package br.com.fiap.hackaton.processor.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Upload {
    private UUID id;
    private List<Video> videos = new ArrayList<>();
    private User user;
    private LocalDateTime creationDate;

    public void addVideo(Video video) {
        videos.add(video);
    }
}
