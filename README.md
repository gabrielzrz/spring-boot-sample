# 🧩 Spring Boot Clean Architecture Project

Este projeto é uma aplicação desenvolvida com **Spring Boot 3.5.6** e **Java 25**, estruturada segundo os princípios da **Arquitetura Limpa (Clean Architecture)**.  
O foco principal é manter um código desacoplado, testável e de fácil manutenção, com camadas bem definidas para domínio, aplicação, infraestrutura e interfaces.

---

## 🚀 Tecnologias

### 🏗️ **Frameworks Principais**
- **Spring Boot Web 3.5.6** — Criação de APIs RESTful.
- **Spring WebFlux** — Suporte reativo e programação assíncrona.
- **Spring Boot DevTools** — Reinicialização automática e LiveReload durante o desenvolvimento.
- **Spring Boot Validation** — Validação de dados com anotações (`@Valid`, `@NotNull`, etc.).
- **Spring Boot Security** — Autenticação e autorização de endpoints.
- **Spring Boot Test / Spring Security Test** — Testes unitários e de integração.

---

### 🗄️ **Banco de Dados e Persistência**
- **PostgreSQL 18** — Banco de dados relacional.
- **Spring Data JPA** — Abstração de persistência com ORM.
- **Hibernate Envers** — Auditoria de entidades.
- **Liquibase** — Controle de versão e migração de schema do banco.
- **P6Spy** — Log detalhado de queries SQL para depuração.

---

### 🧠 **Mapeamento e Serialização**
- **MapStruct** — Conversão entre DTOs e entidades.
- **Jackson (JSON, XML, YAML)** — Serialização e desserialização flexível de dados.

---

### 🔐 **Autenticação e Segurança**
- **JWT (com Auth0 Java-JWT)** — Geração e validação de tokens de autenticação.
- **Spring Security** — Proteção de endpoints e controle de acesso baseado em roles.

---

### ⚡ **Performance e Cache**
- **Spring Cache** — Abstração para caching.
- **Caffeine** — Implementação de cache em memória de alta performance.

---

### 🧩 **Documentação**
- **Springdoc OpenAPI** — Geração automática de documentação Swagger UI.

---

### 🧰 **Utilitários**
- **Apache Commons CSV / POI** — Manipulação de arquivos CSV e XLSX.
- **Apache Commons Collections4** — Estruturas de dados utilitárias.
- **UUID Creator (v7)** — Geração de identificadores únicos universais (UUIDs).
- **Spring HATEOAS** — Criação de APIs REST hipermídia.

---

## 🧱 Arquitetura

O projeto segue os princípios da **Clean Architecture**, com uma divisão clara entre as camadas

---

## 📡 Envio de Logs para Discord

A aplicação utiliza um **Webhook do Discord** configurado para enviar logs de erro automaticamente.  
Isso é feito através da classe anotada com `@RestControllerAdvice` que intercepta exceções globais e envia uma mensagem JSON contendo detalhes do erro via HTTP para o canal configurado.

O envio é realizado **de forma assíncrona** pela annotation `@Async`, garantindo que o processo não bloqueie a resposta ao cliente.

