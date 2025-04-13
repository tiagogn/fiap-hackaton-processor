package br.com.fiap.hackaton.processor.core.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Notification {
    private String to;
    private String subject;
    private String body;
}
