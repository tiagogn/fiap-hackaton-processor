# Projeto Hackaton - SOAT8 - Grupo 18 Pós-Tech - FIAP

![Java](https://img.shields.io/badge/Java-17-blue)
![SpringBoot](https://img.shields.io/badge/SpringBoot-3.4-green)
![Docker](https://img.shields.io/badge/Docker-20.10-blue)
![Postgres](https://img.shields.io/badge/Postgres-16-blue)
![Maven](https://img.shields.io/badge/Maven-3-blue)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=tiagogn_fiap-hackaton-processor&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=tiagogn_fiap-hackaton-processor)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=tiagogn_fiap-hackaton-processor&metric=coverage)](https://sonarcloud.io/summary/new_code?id=tiagogn_fiap-hackaton-processor)

O objetivo do projeto é o processamento de videos:

- processamento de videos

O propjeto foi desenvolvido utilizando as seguintes tecnologias:

- java 17+
- springboot 3.4+
- docker
- banco de dados postgres 16
- FFmpeg

## Executando o projeto localmente

Baixe o projeto no seguinte endereço:

- https://github.com/tiagogn/fiap-hackaton-controller
- https://github.com/tiagogn/fiap-hackaton-processor

em seguinte, dentro da pasta do projeto execute o comando

```shell
idea .
```

com isso, o projeto será aberto dentro da IDE **Intellij**

Para rodar o projeto através do Docker Compose, utilize o seguinte comando:

```shell
docker-compose -f docker-compose.yaml up -d
```

ou execute o seguinte comando na pasta do projeto:

```shell
./mvnw spring-boot:run
```

## SonarQube

Estamos utilizando duas badgets do Sonar para exibição dos quality gates.

## Link do Miro
https://miro.com/app/board/uXjVK5FMZfo=/?share_link_id=705083359492
## Link Video Fase 5
https://www.youtube.com/watch?v=RluRPDqVqfw
