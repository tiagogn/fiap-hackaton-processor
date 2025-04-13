package br.com.fiap.hackaton.processor.infrastructure.repository;

import br.com.fiap.hackaton.processor.core.domain.Upload;
import br.com.fiap.hackaton.processor.core.repository.UploadRepository;
import br.com.fiap.hackaton.processor.infrastructure.repository.entity.UploadEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Qualifier("uploadRepository")
@RequiredArgsConstructor
public class UploadRepositoryPostgres implements UploadRepository{

    private final EntityManager entityManager;

    @Override
    public Upload findById(UUID id) {
        var uploadEntity = entityManager.find(UploadEntity.class, id);
        return uploadEntity.toDomain();
    }

    @Override
    @Transactional
    public void save(Upload upload) {
        var uploadEntity = UploadEntity.fromDomain(upload);
        if (uploadEntity.getId() != null) {
            entityManager.merge(uploadEntity);
        }
        else {
            entityManager.persist(uploadEntity);
        }
    }

}
