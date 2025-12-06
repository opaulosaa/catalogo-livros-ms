# 🧪 Guia de Testes - APIs dos Microserviços

## 📊 Dados Disponíveis

Após executar o script `seed-data.sql`, você terá:
- ✅ **5 usuários** cadastrados
- ✅ **10 livros** no catálogo
- ✅ **5 pagamentos** aprovados
- ✅ **5 notificações** enviadas

---

## 🌐 Endpoints Disponíveis

### 1️⃣ Serviço de Usuários (Porta 8081)

**Base URL:** `http://localhost:8081`

#### Listar Todos os Usuários
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8081/user" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8081/user
```

**Resposta:**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao.silva@email.com",
    "telefone": "85988887777"
  },
  {
    "id": 2,
    "nome": "Maria Santos",
    "email": "maria.santos@email.com",
    "telefone": "85977776666"
  }
  // ... mais usuários
]
```

#### Buscar Usuário por ID
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8081/user/1" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8081/user/1
```

#### Criar Novo Usuário
```powershell
# PowerShell
$body = @{
    nome = "Teste Silva"
    email = "teste@email.com"
    telefone = "85911112222"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8081/user" -Method POST -Body $body -ContentType "application/json"
```

```bash
# CMD/Bash
curl -X POST http://localhost:8081/user ^
  -H "Content-Type: application/json" ^
  -d "{\"nome\":\"Teste Silva\",\"email\":\"teste@email.com\",\"telefone\":\"85911112222\"}"
```

---

### 2️⃣ Serviço de Catálogo de Livros (Porta 8084)

**Base URL:** `http://localhost:8084`

#### Listar Todos os Livros
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8084/livros" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8084/livros
```

**Resposta:**
```json
[
  {
    "id": 1,
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "categoria": "Engenharia de Software",
    "preco": 89.9
  },
  {
    "id": 2,
    "titulo": "Design Patterns",
    "autor": "Gang of Four",
    "categoria": "Engenharia de Software",
    "preco": 95.5
  }
  // ... mais livros
]
```

#### Buscar Livro por ID
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8084/livros/1" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8084/livros/1
```

#### Criar Novo Livro
```powershell
# PowerShell
$body = @{
    titulo = "Test Driven Development"
    autor = "Kent Beck"
    categoria = "Desenvolvimento"
    preco = 75.90
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8084/livros" -Method POST -Body $body -ContentType "application/json"
```

#### Realizar uma Compra
```powershell
# PowerShell
$body = @{
    usuarioId = 1
    livroId = 1
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8084/compras" -Method POST -Body $body -ContentType "application/json"
```

**Resposta:**
```json
{
  "id": 6,
  "usuarioId": 1,
  "livroId": 1,
  "valor": 89.90,
  "status": "APROVED",
  "meioPagamento": "PIX",
  "data": "2025-12-06T17:45:30"
}
```

---

### 3️⃣ Serviço de Pagamento (Porta 8082)

**Base URL:** `http://localhost:8082`

#### Listar Todos os Pagamentos
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8082/pagamentos" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8082/pagamentos
```

**Resposta:**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "livroId": 1,
    "valor": 89.90,
    "status": "APROVED",
    "meioPagamento": "PIX",
    "data": "2025-12-01T17:31:58"
  }
  // ... mais pagamentos
]
```

#### Buscar Pagamento por ID
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8082/pagamentos/1" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8082/pagamentos/1
```

#### Criar Novo Pagamento
```powershell
# PowerShell
$body = @{
    usuarioId = 1
    livroId = 3
    valor = 85.00
    meioPagamento = "CARTAO"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8082/pagamentos" -Method POST -Body $body -ContentType "application/json"
```

---

### 4️⃣ Serviço de Notificações (Porta 8083)

**Base URL:** `http://localhost:8083`

#### Listar Todas as Notificações
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8083/notificacoes" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8083/notificacoes
```

**Resposta:**
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "tipo": "PAGAMENTO",
    "titulo": "Pagamento Aprovado",
    "mensagem": "Seu pagamento para o livro Clean Code no valor de R$ 89,90 foi aprovado!",
    "dados": null,
    "status": "ENVIADA",
    "tentativas": 1,
    "dataCriacao": "2025-12-01T17:31:58"
  }
  // ... mais notificações
]
```

#### Buscar Notificação por ID
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8083/notificacoes/1" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8083/notificacoes/1
```

#### Listar Notificações de um Usuário
```bash
# PowerShell
Invoke-WebRequest -Uri "http://localhost:8083/notificacoes/usuario/1" -Method GET | Select-Object -ExpandProperty Content

# CMD/Bash
curl http://localhost:8083/notificacoes/usuario/1
```

---

## 🔄 Fluxo Completo de Compra

### Passo a Passo para Realizar uma Compra

1️⃣ **Verificar usuários disponíveis:**
```bash
Invoke-WebRequest -Uri "http://localhost:8081/user" -Method GET
```

2️⃣ **Verificar livros disponíveis:**
```bash
Invoke-WebRequest -Uri "http://localhost:8084/livros" -Method GET
```

3️⃣ **Realizar a compra:**
```powershell
$compra = @{
    usuarioId = 1
    livroId = 1
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "http://localhost:8084/compras" -Method POST -Body $compra -ContentType "application/json"
$response.Content
```

4️⃣ **Verificar o pagamento criado:**
```bash
Invoke-WebRequest -Uri "http://localhost:8082/pagamentos" -Method GET
```

5️⃣ **Verificar a notificação gerada:**
```bash
Invoke-WebRequest -Uri "http://localhost:8083/notificacoes/usuario/1" -Method GET
```

---

## 📊 Verificar Dados no Banco

### Conectar ao MySQL do Container
```bash
docker exec -it mysql-db mysql -uroot -proot
```

### Consultas SQL Úteis

```sql
-- Ver usuários
USE usuarios_db;
SELECT * FROM usuarios;

-- Ver livros
USE livros_db;
SELECT * FROM livros;

-- Ver pagamentos
USE pagamento_db;
SELECT * FROM pagamento;

-- Ver notificações
USE notificacao_db;
SELECT * FROM notificacoes;

-- Sair
EXIT;
```

---

## 🛠️ Comandos Úteis

### Ver Logs dos Serviços
```bash
# Todos os serviços
docker-compose logs -f

# Serviço específico
docker-compose logs -f usuarios
docker-compose logs -f catalogo
docker-compose logs -f pagamento
docker-compose logs -f notification
```

### Reiniciar um Serviço
```bash
docker-compose restart usuarios
docker-compose restart catalogo
docker-compose restart pagamento
docker-compose restart notification
```

### Parar Todos os Serviços
```bash
docker-compose down
```

### Iniciar Todos os Serviços
```bash
docker-compose up -d
```

---

## ✅ Validação Completa

Execute este script PowerShell para validar todos os endpoints:

```powershell
Write-Host "`n=== Testando Microserviços ===" -ForegroundColor Green

Write-Host "`n1. Testando Usuários (8081)..." -ForegroundColor Yellow
try {
    $usuarios = Invoke-WebRequest -Uri "http://localhost:8081/user" -Method GET
    Write-Host "✅ Usuários: $($usuarios.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n2. Testando Livros (8084)..." -ForegroundColor Yellow
try {
    $livros = Invoke-WebRequest -Uri "http://localhost:8084/livros" -Method GET
    Write-Host "✅ Livros: $($livros.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n3. Testando Pagamentos (8082)..." -ForegroundColor Yellow
try {
    $pagamentos = Invoke-WebRequest -Uri "http://localhost:8082/pagamentos" -Method GET
    Write-Host "✅ Pagamentos: $($pagamentos.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n4. Testando Notificações (8083)..." -ForegroundColor Yellow
try {
    $notificacoes = Invoke-WebRequest -Uri "http://localhost:8083/notificacoes" -Method GET
    Write-Host "✅ Notificações: $($notificacoes.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Todos os testes concluídos! ===" -ForegroundColor Green
```

---

**✨ Sistema totalmente funcional com dados de teste!**
