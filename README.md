# Sistema de Microsserviços - Catálogo de Livros

## 📋 Visão Geral

Este é um sistema de microsserviços acadêmico para gerenciamento de catálogo de livros, composto por 4 microsserviços que se comunicam via REST APIs.

### Arquitetura

```
┌─────────────────┐     ┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│    USUÁRIOS     │     │    CATÁLOGO     │     │    PAGAMENTO     │     │   NOTIFICAÇÃO   │
│   Porta: 8081   │────▶│   Porta: 8084   │────▶│   Porta: 8082    │────▶│   Porta: 8083   │
└─────────────────┘     └─────────────────┘     └──────────────────┘     └─────────────────┘
        │                       │                        │                         │
        │                       │                        │                         │
        └───────────────────────┴────────────────────────┴─────────────────────────┘
                                          │
                                  ┌───────▼────────┐
                                  │  MySQL 8.0     │
                                  │  Porta: 3306   │
                                  └────────────────┘
                        Databases: usuarios_db, livros_db, 
                                  pagamento_db, notificacao_db
```

## 🚀 Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados
- Portas 8081-8084 e 3306 disponíveis

### Iniciar Todos os Serviços

```bash
docker-compose up --build
```

### Iniciar Serviços Individuais (Desenvolvimento Local)

Certifique-se de que o MySQL está rodando na porta 3306 com o usuário `root` e senha `root`.

```bash
# Terminal 1 - Usuários
cd user_microsservice/usuarios
./mvnw spring-boot:run

# Terminal 2 - Pagamento
cd spring.boot.ms.pagamento
./mvnw spring-boot:run

# Terminal 3 - Notificação
cd AV3_catalogolivro_notification
./mvnw spring-boot:run

# Terminal 4 - Catálogo
cd catalogo_livros
./mvnw spring-boot:run
```

## 📡 Endpoints dos Microsserviços

### 1. Serviço de Usuários (Porta 8081)

**Base URL:** `http://localhost:8081`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/user` | Criar usuário | `{"nome": "string", "email": "string", "telefone": "string"}` |
| GET | `/user` | Listar todos usuários | - |
| GET | `/user/{id}` | Buscar usuário por ID | - |

**Exemplo - Criar Usuário:**
```bash
curl -X POST http://localhost:8081/user \
  -H "Content-Type: application/json" \
  -d '{"nome": "Paulo Silva", "email": "paulo@email.com", "telefone": "85999999999"}'
```

---

### 2. Serviço de Catálogo de Livros (Porta 8084)

**Base URL:** `http://localhost:8084`

#### Gerenciamento de Livros

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/livros` | Criar livro | `{"titulo": "string", "autor": "string", "categoria": "string", "preco": number}` |
| GET | `/livros` | Listar todos livros | - |
| GET | `/livros/{id}` | Buscar livro por ID | - |
| DELETE | `/livros/{id}` | Deletar livro | - |
| GET | `/livros/buscar?titulo={titulo}` | Buscar por título | - |
| GET | `/livros/categoria/{categoria}` | Buscar por categoria | - |

#### Compras

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/compras` | Realizar compra de livro | `{"usuarioId": number, "livroId": number, "valor": number, "meioPagamento": "string"}` |

**Exemplo - Realizar Compra:**
```bash
curl -X POST http://localhost:8084/compras \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "livroId": 1,
    "valor": 49.90,
    "meioPagamento": "CARTAO_CREDITO"
  }'
```

**Meios de Pagamento Aceitos:**
- `CARTAO_CREDITO`
- `CARTAO_DEBITO`
- `PIX`
- `BOLETO`

---

### 3. Serviço de Pagamento (Porta 8082)

**Base URL:** `http://localhost:8082`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/pagamentos` | Processar pagamento | `{"usuarioId": number, "livroId": number, "valor": number, "meioPagamento": "string"}` |
| GET | `/pagamentos` | Listar todos pagamentos | - |
| GET | `/pagamentos/{id}` | Buscar pagamento por ID | - |
| DELETE | `/pagamentos/{id}` | Deletar pagamento | - |

**Nota:** Este serviço é chamado automaticamente pelo endpoint `/compras` do Catálogo. Após processar o pagamento, envia automaticamente uma notificação.

---

### 4. Serviço de Notificação (Porta 8083)

**Base URL:** `http://localhost:8083`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/notificacoes` | Criar notificação | `{"usuarioId": number, "tipo": "string", "titulo": "string", "mensagem": "string", "dados": "string"}` |
| GET | `/notificacoes` | Listar todas notificações | - |
| GET | `/notificacoes/{id}` | Buscar notificação por ID | - |
| GET | `/notificacoes/usuario/{usuarioId}` | Listar notificações de um usuário | - |
| POST | `/notificacoes/reprocessar-pendentes` | Reprocessar notificações pendentes | - |

**Tipos de Notificação:**
- `PAGAMENTO`
- `PEDIDO`
- `SISTEMA`

**Nota:** Este serviço é chamado automaticamente pelo serviço de Pagamento após confirmar um pagamento.

---

## 🔄 Fluxo de Compra Completo

1. **Cliente cria um usuário** (se ainda não existir)
   ```
   POST http://localhost:8081/user
   ```

2. **Cliente lista livros disponíveis**
   ```
   GET http://localhost:8084/livros
   ```

3. **Cliente realiza uma compra**
   ```
   POST http://localhost:8084/compras
   ```
   
4. **Fluxo automático:**
   - Catálogo valida o livro
   - Catálogo chama o serviço de Pagamento (8082)
   - Pagamento processa e salva
   - Pagamento chama o serviço de Notificação (8083)
   - Notificação valida o usuário (consulta serviço 8081)
   - Notificação envia notificação ao usuário
   - Resposta retorna ao cliente

5. **Cliente pode consultar a notificação**
   ```
   GET http://localhost:8083/notificacoes/usuario/{usuarioId}
   ```

---

## 🗄️ Estrutura dos Bancos de Dados

### usuarios_db
```sql
CREATE TABLE usuarios (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  telefone VARCHAR(50)
);
```

### livros_db
```sql
CREATE TABLE livros (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  titulo VARCHAR(255),
  autor VARCHAR(255),
  categoria VARCHAR(100),
  preco DOUBLE
);
```

### pagamento_db
```sql
CREATE TABLE pagamento (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  valor DECIMAL(10,2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  data DATETIME NOT NULL,
  meio_pagamento VARCHAR(50) NOT NULL,
  usuario_id BIGINT NOT NULL,
  livro_id BIGINT NOT NULL
);
```

### notificacao_db
```sql
CREATE TABLE notificacoes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  usuario_id BIGINT,
  tipo VARCHAR(50),
  titulo VARCHAR(255),
  mensagem VARCHAR(1000),
  dados VARCHAR(2000),
  status VARCHAR(50),
  tentativas INT,
  data_criacao DATETIME
);
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0 / 3.5.7 / 4.0.0**
- **Spring Data JPA**
- **Spring Web**
- **MySQL 8.0**
- **Docker & Docker Compose**
- **Maven**

---

## 📝 Variáveis de Ambiente

As seguintes variáveis podem ser configuradas (com valores padrão):

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| DB_HOST | localhost | Host do MySQL |
| DB_USER | root | Usuário do MySQL |
| DB_PASSWORD | root | Senha do MySQL |
| USUARIOS_HOST | localhost | Host do serviço de usuários |
| PAGAMENTO_HOST | localhost | Host do serviço de pagamento |
| NOTIFICACAO_HOST | localhost | Host do serviço de notificação |

---

## 🧪 Testando o Sistema

### Teste Rápido com cURL

```bash
# 1. Criar usuário
curl -X POST http://localhost:8081/user \
  -H "Content-Type: application/json" \
  -d '{"nome": "João Silva", "email": "joao@email.com", "telefone": "85988888888"}'

# 2. Criar livro
curl -X POST http://localhost:8084/livros \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Clean Code", "autor": "Robert Martin", "categoria": "Tecnologia", "preco": 89.90}'

# 3. Realizar compra
curl -X POST http://localhost:8084/compras \
  -H "Content-Type: application/json" \
  -d '{"usuarioId": 1, "livroId": 1, "valor": 89.90, "meioPagamento": "PIX"}'

# 4. Verificar notificação
curl http://localhost:8083/notificacoes/usuario/1
```

---

## 🐛 Troubleshooting

### Erro de conexão com banco de dados
- Verifique se o MySQL está rodando
- Verifique as credenciais (root/root)
- Verifique se os databases foram criados (init.sql)

### Serviços não se comunicam
- Verifique se todos os serviços estão rodando
- No Docker, use os nomes dos containers: `usuarios`, `pagamento`, `notification`, `catalogo`
- Localmente, use `localhost`

### Porta já em uso
```bash
# Windows
netstat -ano | findstr :<porta>
taskkill /PID <pid> /F

# Linux/Mac
lsof -i :<porta>
kill -9 <pid>
```

---

## 👥 Projeto Acadêmico

Este é um projeto acadêmico desenvolvido para demonstrar conceitos de arquitetura de microsserviços e comunicação via REST APIs.

**Observações:**
- A segurança não foi implementada em profundidade (projeto acadêmico)
- As notificações são simuladas via console (não há integração com provedores de email)
- Tratamento de erros é básico

---

## 📄 Licença

Projeto acadêmico - Unifor 2025
#   a v 3 _ p r o j _ s i s t e m a s  
 