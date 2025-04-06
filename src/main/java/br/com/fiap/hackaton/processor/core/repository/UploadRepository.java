package br.com.fiap.hackaton.processor.core.repository;

import br.com.fiap.hackaton.processor.core.domain.Upload;

import java.util.UUID;

public interface UploadRepository {
    Upload findById(UUID id);
    void save(Upload upload);
}
