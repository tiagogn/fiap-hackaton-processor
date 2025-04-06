package br.com.fiap.hackaton.processor.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Video {
    private UUID id;
    private String name;
    private Integer size;
    private String contentType;
    private String uri;
    private ByteArrayInputStream inputStream;
    private StatusVideo status;
}
