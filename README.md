# Projeto Hackaton - SOAT8 - Grupo 18 Pós-Tech - FIAP

![Java](https://img.shields.io/badge/Java-17-blue)
![SpringBoot](https://img.shields.io/badge/SpringBoot-3.4-green)
![Docker](https://img.shields.io/badge/Docker-20.10-blue)
![Postgres](https://img.shields.io/badge/Postgres-16-blue)
![Maven](https://img.shields.io/badge/Maven-3-blue)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=tiagogn_fiap-hackaton-processor&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=tiagogn_fiap-hackaton-processor)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=tiagogn_fiap-hackaton-processor&metric=coverage)](https://sonarcloud.io/summary/new_code?id=tiagogn_fiap-hackaton-processor)

## Problema
O Grupo 18 da Pós-Graduação em Arquitetura de Software da FIAP busca aprimorar seu projeto de processamento de imagens. Atualmente, o grupo conta com um protótipo simples capaz de processar um vídeo e gerar um arquivo .zip contendo as imagens extraídas. O objetivo agora é desenvolver uma versão mais robusta, permitindo o envio de vídeos e o download do arquivo .zip diretamente. Essa nova versão deverá incorporar conceitos como desenho de arquitetura, desenvolvimento de microsserviços, qualidade de software, mensageria e segurança.

## Escopo
O objetivo do projeto é o processamento de videos:

- processamento de videos

## Requisitos Funcionais e Técnicos

Funcionalidades principais:

•	Processamento simultâneo de múltiplos vídeos.

•	Garantia de que nenhuma requisição seja perdida, mesmo em situações de pico.

•	Implementação de autenticação por usuário e senha para maior segurança.

•	Exibição de uma listagem com o status dos vídeos de cada usuário.

•	Notificação ao usuário em caso de erro (via e-mail ou outros meios de comunicação).

Requisitos técnicos:

•	Persistência dos dados.

•	Arquitetura escalável para suportar crescimento e maior demanda.

•	Versionamento do projeto no GitHub.

•	Garantia de qualidade por meio de testes bem estruturados.

•	Implementação de CI/CD (Integração Contínua e Entrega Contínua).

## Entregáveis

1.	Documentação: Descrição da arquitetura proposta e detalhes técnicos.
2.	Scripts: Arquivos para criação do banco de dados e de outros recursos utilizados.
3.	Repositório: Link do projeto versionado no GitHub.
4.	Apresentação: Vídeo de até 10 minutos apresentando a documentação, a arquitetura escolhida e o sistema em funcionamento.

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

## Arquitetura To Be Kubernetes na CLOUD
![Diagrama Fase Hackaton.jpg](Diagrama%20Fase%20Hackaton.jpg)

## SonarQube

Estamos utilizando duas badgets do Sonar para exibição dos quality gates.

## Link do Miro
https://miro.com/app/board/uXjVK5FMZfo=/?share_link_id=705083359492
## Link Video Fase 5
https://www.youtube.com/watch?v=RluRPDqVqfw
