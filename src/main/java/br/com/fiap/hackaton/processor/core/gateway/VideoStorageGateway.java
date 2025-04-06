package br.com.fiap.hackaton.processor.core.gateway;

import br.com.fiap.hackaton.processor.core.domain.Upload;

public interface VideoStorageGateway {
    void readAllBytesByUpload(Upload upload);
    void writeFile(String fileName, byte[] bytes, Upload upload);
}
