package br.com.fiap.hackaton.processor.core.processor.video;

import br.com.fiap.hackaton.processor.core.domain.Video;

public interface SliceVideo {
    String execute(String key, Video video);
}
