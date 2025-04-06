package br.com.fiap.hackaton.processor.infrastructure.repository.entity;

import br.com.fiap.hackaton.processor.core.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User toDomain(){
        var user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setCpf(cpf);
        return user;
    }

    public static UserEntity fromDomain(User user){
        var userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setCpf(user.getCpf());
        return userEntity;
    }
}
