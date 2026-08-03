# `liquibase:diff` não gera changesets pra tabela/coluna nova (bug upstream, sem fix completo)

## Contexto

Durante a migração do projeto pro Spring Boot 4 (`pom.xml`, parent `spring-boot-starter-parent:4.1.0`), a dependência do Liquibase foi trocada de `org.liquibase:liquibase-core` solto pra `org.springframework.boot:spring-boot-starter-liquibase` (`pom.xml` linhas ~105-108), deixando de fixar `liquibase.version` manualmente e passando a seguir a versão gerenciada pelo Boot (**5.0.3** no Boot 4.1.0). O `liquibase-maven-plugin` (usado via `./mvnw liquibase:update|status|diff`, configurado em `pom.xml` dentro de `<build><plugins>`) foi alinhado pra usar essa mesma versão via `${liquibase.version}`.

Nessa troca, dois problemas de compatibilidade com Spring Boot 4 apareceram e **foram corrigidos** (não são o assunto principal deste doc, só contexto):

1. `jakarta.persistence.PersistenceConfiguration` ausente — `hibernate-core:6.6.15.Final` hardcoda `jakarta.persistence-api:3.1.0` no próprio pom, mas o Boot 4.1 gerencia `3.2.0`. Fix: dependência explícita de `jakarta.persistence-api` no bloco `<dependencies>` do plugin, pinada em `${jakarta-persistence.version}`.
2. `SpringImplicitNamingStrategy` não encontrada — a classe mudou de pacote no Boot 4.1 (`org.springframework.boot.orm.jpa.hibernate.*` → `org.springframework.boot.hibernate.*`). Fix: `src/main/resources/db/liquibase.properties`, propriedade `referenceUrl`, parâmetro `hibernate.implicit_naming_strategy`.
3. `jboss-logging` incompatível — mesma causa do item 1 (`hibernate-core` hardcoda `3.5.0.Final`, Boot 4.1 gerencia `3.6.3.Final`). Fix: dependência explícita no plugin, pinada em `${jboss-logging.version}`.

Padrão comum aos três: dependências declaradas dentro de `<plugin><dependencies>` **não recebem** o `dependencyManagement` do projeto/BOM do Spring Boot — qualquer transitiva hardcoded num pom de terceiro (como `hibernate-core`) fica presa na versão errada só dentro do plugin, mesmo com a aplicação normal resolvendo certo.

## O bug real: dois problemas distintos no `liquibase:diff`

Depois dos fixes acima, `./mvnw liquibase:diff` passou a rodar sem erro — mas com **dois comportamentos incorretos**, que são bugs upstream (não config nossa):

### Problema 1 — `dropPrimaryKey` falso positivo pra tabelas já existentes (RESOLVIDO)

Rodando `diff` comparando o banco real (Postgres, com PKs nomeadas explicitamente tipo `permissionPK`, `personPK` — criadas assim nos changelogs originais `1-create-insert-default-users-and-branch.xml` a `4-create-parameter.xml`) contra a referência Hibernate (`referenceUrl=hibernate:spring:br.com.gabrielzrz.entity?...`), o Liquibase propunha **dropar todas as 15-16 primary keys existentes**.

**Causa:** JPA/Hibernate não tem conceito de "nome de constraint de primary key" — `@Id` só diz qual coluna é PK, sem nome. A interface `ImplicitNamingStrategy` do Hibernate tem hooks pra nomear FK (`determineForeignKeyName`), unique constraint (`determineUniqueKeyName`) e index (`determineIndexName`), mas **não tem** equivalente pra PK. Então o lado "referência" (Hibernate) sempre chega sem nome de PK, e o `diff` interpreta a PK nomeada do banco real como "constraint extra que a referência não conhece" → propõe `dropPrimaryKey`.

**Confirmado por issue externo:** [jhipster/generator-jhipster#33501](https://github.com/jhipster/generator-jhipster/issues/33501) — mesmo sintoma ao migrar Hibernate 6→7, com a recomendação oficial da comunidade (não um fix do time do Liquibase, um comentário de usuário no issue):

> "add configuration to the liquibase-maven-plugin that excludes primaryKeys and uniqueConstraints from diff"

**Fix aplicado** — `src/main/resources/db/liquibase.properties`:

```properties
diffTypes=tables, columns, views, indexes, foreignkeys, uniqueconstraints
```

(lista padrão do Liquibase menos `primarykeys`). Testamos incluir `uniqueconstraints` de volta pra ver se o mesmo problema acontecia com `uk_parameter_name_branch` (constraint nomeada manualmente em `parameter`) — **não aconteceu**, porque unique constraint é nomeável via anotação JPA (`@UniqueConstraint(name=...)`), diferente de PK. Por isso só `primarykeys` precisou ser excluído aqui.

**Efeito colateral aceito:** excluir `primarykeys` do diff não afeta a criação de PK em tabela nova (isso vai embutido no `createTable` via `<constraints primaryKey="true">`, categoria `tables`/`columns`, que continua no diff) — só desliga a detecção de *mudança* de PK em tabela que já existe nos dois lados. Só que, na prática, isso ficou irrelevante por causa do Problema 2 abaixo.

### Problema 2 — tabela/coluna genuinamente nova não gera changeset (SEM FIX)

Criamos uma entidade de teste (`Boa.java`, `@Entity @Table(name="boa")`) e uma coluna nova (`Branch.java`, campo `teste`) pra validar o fluxo de `diff`. Resultado: o Hibernate reconhece os dois normalmente nos logs (`Found table boa`, `Found column teste varchar(100)`), o banco real **genuinamente não tem** nem a tabela nem a coluna (confirmado via `psql`), mas o changelog gerado fica vazio — `changeSets count: 0`, `No changesets to add to the changelog output.`

**Causa raiz (rastreada até o commit exato):**

1. Em [liquibase/liquibase-hibernate#708](https://github.com/liquibase/liquibase-hibernate/issues/708) (**aberto até hoje**), o usuário `lorenzbaier` identificou que um commit do `liquibase-core` 4.28 mudou como `SnapshotControl` filtra tipos de objeto por catálogo.
2. `HibernateDatabase.supportsCatalogs()` retorna `false`. Isso faz o `CatalogSnapshotGenerator` do lado da referência (Hibernate) nunca ser chamado — o catálogo da referência fica algo tipo `*hibernate*.DEFAULT`, enquanto o catálogo do alvo real é `DEFAULT.DEFAULT`.
3. Como os dois catálogos não batem, `DiffOutputControl.shouldOutput()` retorna `false` **só pras operações de ADD** (`createTable`, `addColumn`) — por isso `dropPrimaryKey` sempre funcionou certo (não depende de comparar catálogo da referência) mas `createTable`/`addColumn` pra objeto novo nunca aparecem.

**Workaround da comunidade** (comentário de `Guschtel` no #708, nunca incorporado oficialmente): sobrescrever `supportsCatalogs()` pra retornar `true` em `HibernateDatabase`, ou fazer `getDefaultCatalogName()`/`getDefaultSchemaName()` retornarem `null` em `HibernateSpringPackageDatabase`. **Exige compilar um fork próprio do `liquibase-hibernate6`** — não é configurável via `liquibase.properties`, inviável de manter no dia a dia.

**Issue relacionado, aparentemente fechado mas não realmente resolvido:** [liquibase/liquibase#6148](https://github.com/liquibase/liquibase/issues/6148) — "Changeset generation broken since 4.28". Fechado dizendo que a correção veio no PR [liquibase-hibernate#717](https://github.com/liquibase/liquibase-hibernate/pull/717), lançada na **4.30.0**. A reprodução mínima desse issue (sem `liquibase-hibernate6`, só JDBC-a-JDBC) realmente funciona depois do fix — mas o **#708, que é o caso com `liquibase-hibernate6` (o nosso caso), continua aberto**. O #717 resolveu só o cenário mais estreito do #6148, não o caso geral com a extensão Hibernate.

**Testamos reverter pra `liquibase.version=4.27.0`** (versão anterior à regressão) pra ver se contornava — **não resolveu, e piorou em parte**:
- `Boa` (a entidade nova) **nem apareceu** na lista de entidades reconhecidas pelo `liquibase-hibernate6:4.27.0` — extensão velha demais, incompatível com o Hibernate ORM 6.6.15.Final que o projeto usa hoje.
- `teste` (a coluna nova) continuou sendo encontrada nos logs mas **sem virar changeset**, exatamente como na 5.0.3.

Ou seja, a versão 4.27.0 não é mais uma opção viável neste projeto (o Hibernate já subiu de versão desde então) — e mesmo se fosse, não resolveria o Problema 2.

## Estado atual (o que ficou configurado)

- `pom.xml`: sem `liquibase.version` fixado nas properties — Liquibase segue a versão gerenciada pelo Boot (5.0.3 no Boot 4.1.0). O `<plugin>` do `liquibase-maven-plugin` tem `<version>${liquibase.version}</version>` explícito (evita ambiguidade de resolução de versão de plugin via herança de `pluginManagement`).
- `src/main/resources/db/liquibase.properties`: `diffTypes` excluindo `primarykeys` (Problema 1 resolvido).
- **Problema 2 permanece sem solução configurável.** Na prática: `liquibase:diff` continua confiável pra achar divergências em objetos que **já existem nos dois lados** (funciona bem, testado com as PKs). Pra **tabela nova / coluna nova**, `diff` não é confiável — o changelog tem que ser escrito manualmente, seguindo o padrão dos changelogs `1-4` já existentes (`createTable` com `<constraints primaryKey="true" primaryKeyName="NomePK"/>` inline no id, etc.).

## Como voltar pra `liquibase-core` 4.27.0 (sem `spring-boot-starter-liquibase`)

Se no futuro quiser testar de novo com a lib original do Liquibase (em vez do `spring-boot-starter-liquibase`, que é o módulo do próprio Spring Boot que agrega `liquibase-core` + `spring-boot-jdbc` etc.), o roteiro é:

**1. `pom.xml` → `<properties>`:** fixar a versão manualmente (hoje não existe essa property, o Liquibase segue o que o `spring-boot-starter-parent:4.1.0` gerencia, que é `5.0.3`):

```xml
<liquibase.version>4.27.0</liquibase.version>
```

Essa property tem efeito automático em dois lugares, porque o `dependencyManagement` herdado do `spring-boot-dependencies` (BOM do Boot) também usa internamente um placeholder de nome `${liquibase.version}` pra gerenciar `org.liquibase:liquibase-core` — redefinir a property localmente sobrescreve esse valor em todo o build:
- na dependência principal (item 2 abaixo);
- no `<plugin>` do `liquibase-maven-plugin` (`pom.xml` linha 191, `<version>${liquibase.version}</version>`, já fica pinado sozinho).

**2. `pom.xml` → `<dependencies>` (hoje nas linhas ~103-112):** trocar

```xml
<!--Liquibase-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-liquibase</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-liquibase-test</artifactId>
    <scope>test</scope>
</dependency>
```

por

```xml
<!--Liquibase-->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

(sem `<version>` — resolve via `${liquibase.version}` do passo 1. Não existe um equivalente de `spring-boot-starter-liquibase-test` pra lib pura; se precisar de suporte de teste, seria outra dependência, não tratado aqui.)

**Diferença entre as duas libs** (contexto de uma pergunta anterior nessa mesma conversa): `liquibase-core` é a lib "crua" publicada pelo próprio projeto Liquibase, sem nenhuma integração com Spring — só o engine de migration. `spring-boot-starter-liquibase` é um agregador do Boot que traz `liquibase-core` + `spring-boot-starter-jdbc` + `spring-boot-jdbc` + o módulo `spring-boot-liquibase` (que carrega a `LiquibaseAutoConfiguration`). Funcionalmente as props `spring.liquibase.*` do `application.properties` funcionam igual nos dois casos — a diferença real é só de governança de versão (quem decide a versão do Liquibase: você, via property, ou o Boot, via BOM).

**Atenção:** como documentado no "Problema 2" acima, voltar pra 4.27.0 **não resolve** o bug de tabela/coluna nova — testamos e piorou (o `liquibase-hibernate6:4.27.0` nem reconhece entidade nova, por ser velho demais pro Hibernate ORM 6.6.15.Final que o projeto usa hoje). Esse roteiro serve só se quiser comparar de novo no futuro, não é uma recomendação de fix.

## Dependências extras adicionadas no plugin (necessárias independente da versão do Liquibase)

Duas dependências foram adicionadas dentro do `<plugin><dependencies>` do `liquibase-maven-plugin` (`pom.xml`, dentro do bloco que já tinha `spring-boot-starter-data-jpa`, `liquibase-core`, `liquibase-hibernate6`, `jakarta.validation-api`, `javassist`):

```xml
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>${jakarta-persistence.version}</version>
</dependency>
<dependency>
    <groupId>org.jboss.logging</groupId>
    <artifactId>jboss-logging</artifactId>
    <version>${jboss-logging.version}</version>
</dependency>
```

**Importante: essas duas dependências não são específicas de usar Liquibase acima da 4.27 — testamos e o erro acontece igual na 4.27.0.** A causa é o upgrade pro Spring Boot 4.1, não a versão do Liquibase:

- **`jakarta.persistence-api`** — o plugin declara `spring-boot-starter-data-jpa` com `<version>${project.parent.version}</version>` (ou seja, `4.1.0`, a versão do Boot). Essa dependência traz transitivamente `hibernate-core:6.6.15.Final`, que **hardcoda no próprio pom publicado** a versão `jakarta.persistence-api:3.1.0`. Só que o Boot 4.1 gerencia (e o restante do projeto usa) a `3.2.0`, que tem a classe `jakarta.persistence.PersistenceConfiguration` — sem essa dependência explícita, o plugin quebra com `NoClassDefFoundError`/"A required class was missing ... jakarta/persistence/PersistenceConfiguration" ao tentar rodar `liquibase:diff`.
- **`jboss-logging`** — mesmo mecanismo: `hibernate-core:6.6.15.Final` hardcoda `jboss-logging:3.5.0.Final`, mas o Boot 4.1 gerencia `3.6.3.Final` (que tem o método `Logger.getMessageLogger(MethodHandles.Lookup, Class, String)` que o Hibernate 6.6.x espera). Sem essa dependência explícita, o plugin quebra com `NoSuchMethodError` ao tentar montar o `SessionFactory` (erro: "Error activating Bean Validation integration").

**Por que isso só afeta o plugin, não a aplicação normal:** dependências declaradas dentro de `<plugin><dependencies>` **não recebem** o `dependencyManagement`/BOM do projeto — no build normal da aplicação, o Maven corrigiria essas versões transitivas erradas automaticamente (porque o `spring-boot-dependencies` gerencia `jakarta-persistence.version=3.2.0` e `jboss-logging.version=3.6.3.Final`); dentro do plugin, essa correção não é aplicada, e a versão hardcoded do `hibernate-core` "vaza" sem ninguém pra ajustar, a não ser que a gente declare a dependência certa manualmente ali dentro. Por isso, **mesmo revertendo pra `liquibase-core` 4.27.0 (seção acima), essas duas dependências extras continuam sendo necessárias** — elas resolvem um problema do Spring Boot 4.1 + `spring-boot-starter-data-jpa` dentro do plugin, não um problema do Liquibase em si.

Além dessas duas, também foi corrigida a referência de pacote da `SpringImplicitNamingStrategy` no `db/liquibase.properties` (`org.springframework.boot.orm.jpa.hibernate.*` → `org.springframework.boot.hibernate.*`, mesma causa: reorganização de pacotes do Boot 4.1) — não é uma dependência de plugin, mas é do mesmo lote de fixes de compatibilidade com Boot 4.

## Pra revisitar no futuro

- Acompanhar [liquibase/liquibase-hibernate#708](https://github.com/liquibase/liquibase-hibernate/issues/708) — se fechar de verdade (com fix, não só stale-bot), testar `diff` de novo pra tabela/coluna nova antes de continuar escrevendo changelog na mão.
- Se o volume de entidades novas justificar, considerar aplicar o patch da comunidade (`supportsCatalogs()` → `true`) como um fork/build customizado do `liquibase-hibernate6` — avaliar o custo de manutenção vs. o ganho.
- Reavaliar se vale a pena manter `liquibase-hibernate6` como estratégia de referência pra diff, ou migrar pra outra abordagem (ex: manter muitos changelogs incrementais escritos à mão desde já, sem depender de diff pra novidades — só usar diff como *sanity check* de tabelas existentes).