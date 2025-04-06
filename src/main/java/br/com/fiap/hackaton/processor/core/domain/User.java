package br.com.fiap.hackaton.processor.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String email;
    private String cpf;
}
