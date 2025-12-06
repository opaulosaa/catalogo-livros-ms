# Catálogo de Livros

> Microserviço simples para gerenciar um catálogo de livros — CRUD básico usando Spring Boot + Spring Data JPA + MySQL.

## Visão geral

O projeto fornece uma API REST mínima para criar, listar, buscar e deletar livros. Cada livro possui título, autor, categoria e preço. A aplicação foi criada com Spring Boot 3.5.7 e está preparada para rodar com Java 17+ (recomendado).

## Tecnologias

- Java 17+
- Spring Boot 3.5.7
- Spring Data JPA
- MySQL (driver `mysql-connector-j`)
- Maven

## Estrutura do Projeto

```
catalogo_livros/
├── src/
│   ├── main/
│   │   ├── java/com/catalogo/livros/
│   │   │   ├── CatalogoLivrosApplication.java     # Classe principal
│   │   │   ├── config/
│   │   │   │   └── CorsConfig.java                # Configuração CORS
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java            # Controller home
│   │   │   │   └── LivroController.java           # Controller REST
│   │   │   ├── model/
│   │   │   │   └── Livro.java                     # Entidade JPA
│   │   │   ├── repository/
│   │   │   │   └── LivroRepository.java           # Repositório JPA
│   │   │   └── service/
│   │   │       └── LivroService.java              # Serviço de negócio
│   │   └── resources/
│   │       ├── application.properties             # Configurações
│   │       ├── static/                            # Arquivos estáticos (CSS, JS)
│   │       └── templates/
│   │           └── index.html                     # Template HTML
│   └── test/
│       └── java/com/catalogo/livros/
│           └── CatalogoLivrosApplicationTests.java # Testes
├── pom.xml                                         # Dependências Maven
├── mvnw                                            # Maven wrapper (Linux/Mac)
├── mvnw.cmd                                        # Maven wrapper (Windows)
├── HELP.md                                         # Documentação de ajuda
└── README.md                                       # Este arquivo
```

## Estrutura principal

- `src/main/java/com/catalogo/livros` — código-fonte
  - `controller/LivroController.java` — expõe os endpoints REST
  - `service/LivroService.java` — lógica de aplicação
  - `repository/LivroRepository.java` — repositório JPA
  - `model/Livro.java` — entidade JPA (id, titulo, autor, categoria, preco)
- `src/main/resources/application.properties` — configurações (datasource, JPA, porta)

## Modelo `Livro`

Campos:

- `id` (Long) — chave autogerada
- `titulo` (String)
- `autor` (String)
- `categoria` (String)
- `preco` (Double)

## Endpoints

Base: `http://localhost:8080/livros`

- `POST /livros`
  - Cria um novo livro. Enviar JSON no body. Exemplo:
  ```json
  {
    "titulo": "Exemplo",
    "autor": "Autor",
    "categoria": "Ficcao",
    "preco": 29.9
  }
  ```

- `GET /livros`
  - Retorna lista com todos os livros.

- `GET /livros/{id}`
  - Retorna um livro por `id`.

- `DELETE /livros/{id}`
  - Deleta o livro com o `id` informado.

- `GET /livros/buscar?titulo=...`
  - Busca livros cujo título contém o valor informado.

- `GET /livros/categoria/{categoria}`
  - Retorna livros de uma categoria específica.

## Configuração do banco (padrão)

Arquivo: `src/main/resources/application.properties`

Configuração de exemplo já presente no projeto (ajuste usuário/senha/URL conforme seu ambiente):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/catalogo_livros?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

Observação: quando `spring.jpa.hibernate.ddl-auto=update`, o Hibernate cria/atualiza as tabelas automaticamente — use com cuidado em produção.

## Como compilar e executar

Pré-requisitos:

- Java 17 ou superior instalado (recomendado) — ver `java -version`
- Maven instalado (ou use `./mvnw` / `mvnw.cmd` no Windows)
- MySQL rodando com banco `catalogo_livros` (ou ajuste a URL no `application.properties`)

Build com Maven:

```powershell
cd "C:\caminho\para\projeto\livros"
mvn clean package -DskipTests
```

Executar o JAR (exemplo usando Java do sistema):

```powershell
java -jar target\livros-0.0.1-SNAPSHOT.jar
```

Se o seu sistema tiver múltiplas versões de Java e você quiser usar a JRE empacotada no VS Code, por exemplo, rode:

```powershell
& 'C:\Users\maria\.vscode\extensions\redhat.java-1.47.0-win32-x64\jre\21.0.8-win32-x86_64\bin\java.exe' -jar target\livros-0.0.1-SNAPSHOT.jar
```

## Executando em desenvolvimento (com reloading)

Você pode usar o `spring-boot-devtools` (já incluído) ao executar a aplicação a partir da IDE para reload automático.

## Observações e próximos passos

- A versão do Spring Boot (3.5.7) requer Java 17+. Recomenda-se manter `pom.xml` com `<java.version>17</java.version>`.
- Recomendo configurar credenciais do banco em variáveis de ambiente ou `application-{profile}.properties` para não deixar senhas em repositório.
- Poderíamos adicionar testes de integração, DTOs e tratamento de erros (ex.: retornar 404 quando não encontrado) como melhorias.

## Integração com Microsserviços

Este microsserviço faz parte de um **ecossistema de microsserviços** para um sistema de e-commerce de livros. Futuramente, o **Serviço de Livros** se conectará com os seguintes microsserviços:

### 1. **Serviço Livros** (Este projeto) — Maria Clara
- **Base path:** `/livros`
- **Responsabilidade:** Manter o catálogo de livros — CRUD básico e pesquisa
- **Endpoints:**
  - `POST /livros` — criar livro
  - `GET /livros` — listar todos
  - `GET /livros/{id}` — buscar por id
  - `DELETE /livros/{id}` — remover livro
  - `GET /livros/buscar?titulo=...` — buscar por título
  - `GET /livros/categoria/{categoria}` — listar por categoria
- **Modelo:** id, titulo, autor, categoria, preco
- **Regras:** titulo e autor obrigatórios; preco >= 0
- **Integrações:** Será consultado pelo Serviço de Pedidos

### 2. **Serviço Usuários** — Savas Neto
- **Base path:** `/usuarios`
- **Responsabilidade:** Gerenciar cadastro dos usuários
- **Endpoints:**
  - `POST /usuarios` — criar usuário
  - `GET /usuarios` — listar todos
  - `GET /usuarios/{id}` — buscar por id
- **Modelo:** id, nome, email, telefone
- **Regras:** email único; validação de formato
- **Integrações:** Usado por Pedidos e Pagamento

### 3. **Serviço Pedidos** — Levi de Pontes
- **Base path:** `/pedidos`
- **Responsabilidade:** Criar e listar pedidos
- **Endpoints:**
  - `POST /pedidos/{usuarioId}/{livroId}` — criar pedido
  - `GET /pedidos` — listar todos
  - `GET /pedidos/{id}` — buscar por id
  - `PUT /pedidos/{id}/status` — atualizar status
- **Modelo:** id, usuarioId, livroId, preco, status, dataCriacao
- **Regras:** Valida existência do livro e usuário; inicia como PENDENTE
- **Integrações:** Chama Usuários e Livros; atualizado por Pagamento

### 4. **Serviço Pagamento** — Paulo Alencar
- **Base path:** `/pagamentos`
- **Responsabilidade:** Processar pagamentos
- **Endpoints:**
  - `POST /pagamentos` — criar pagamento
  - `GET /pagamentos/{id}` — buscar por id
  - `GET /pagamentos/usuario/{usuarioId}` — pagamentos do usuário
  - `GET /pagamentos/pedido/{pedidoId}` — pagamento do pedido
- **Modelo:** id, pedidoId, usuarioId, metodo, valor, status, data, referencia
- **Regras:** Valida pedido + usuário; processa e aprova pagamento; ao aprovar notifica e atualiza pedido
- **Integrações:** Pedidos, Usuários, Notificação

### 5. **Serviço Notificação** — Arthur Wermont
- **Base path:** `/notificacoes`
- **Responsabilidade:** Enviar e registrar notificações
- **Endpoints:**
  - `POST /notificacoes` — criar notificação
  - `GET /notificacoes/{id}` — buscar por id
  - `GET /notificacoes/usuario/{usuarioId}` — notificações do usuário
  - `POST /notificacoes/webhook` — receber eventos
- **Modelo:** id, usuarioId, tipo, titulo, mensagem, dados, status, tentativas, dataCriacao
- **Regras:** Enfileira e tenta envio; até 3 tentativas; deduplicação básica
- **Integrações:** Recebe eventos de Pagamento e Pedidos

### Diagrama de Integração

```
┌─────────────────┐
│  Livros (Maria) │
└────────┬────────┘
         │
         ├──────────────────────┐
         │                      │
    ┌────▼────────┐      ┌──────▼──────┐
    │  Pedidos     │      │  Usuários   │
    │  (Levi)      │      │  (Savas)    │
    └────┬────────┘      └──────┬──────┘
         │                      │
         └──────────┬───────────┘
                    │
              ┌─────▼─────────┐
              │  Pagamento    │
              │  (Paulo)      │
              └─────┬─────────┘
                    │
              ┌─────▼──────────┐
              │ Notificação    │
              │ (Arthur)       │
              └────────────────┘
```

## Contato

Projeto mantido por quem o enviou ao repositório. Para ajustes no `application.properties` ou deploy, entre em contato com o autor.
