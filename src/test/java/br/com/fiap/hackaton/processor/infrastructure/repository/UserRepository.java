package br.com.fiap.hackaton.processor.infrastructure.repository;

import br.com.fiap.hackaton.processor.infrastructure.repository.entity.UserEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
