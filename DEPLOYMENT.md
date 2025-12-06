# 🚀 Documentação de Deployment - Microserviços

## ✅ Status do Deployment

**Data:** 06/12/2025  
**Status:** ✅ **SUCESSO** - Todos os serviços estão rodando corretamente

---

## 📦 Containers Deployados

| Serviço | Container | Porta | Status | Imagem |
|---------|-----------|-------|--------|--------|
| MySQL Database | `mysql-db` | 3307:3306 | ✅ Healthy | mysql:8.0 |
| Usuários | `usuarios-service` | 8081 | ✅ Running | trabalhofinal-av3-usuarios |
| Pagamento | `pagamento-service` | 8082 | ✅ Running | trabalhofinal-av3-pagamento |
| Notificação | `notification-service` | 8083 | ✅ Running | trabalhofinal-av3-notification |
| Catálogo | `catalogo-service` | 8084 | ✅ Running | trabalhofinal-av3-catalogo |

---

## 🔗 URLs dos Serviços

### 🌐 Base URLs
- **Usuários:** http://localhost:8081
- **Pagamento:** http://localhost:8082
- **Notificação:** http://localhost:8083
- **Catálogo:** http://localhost:8084

### 🗄️ Banco de Dados
- **Host:** localhost
- **Porta:** 3307
- **Usuário:** root
- **Senha:** root
- **Databases:**
  - `usuarios_db`
  - `pagamento_db`
  - `notificacao_db`
  - `livros_db`

---

## 🔄 Fluxo de Comunicação

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │
       v
┌─────────────────────────────────────────┐
│  Catálogo Service (8084)                │
│  - GET /livros (listar livros)          │
│  - POST /compras (realizar compra)      │
└──────────────┬──────────────────────────┘
               │
               v
       ┌───────────────┐
       │  Pagamento    │ (8082)
       │  Service      │
       └───────┬───────┘
               │
               v
       ┌───────────────┐
       │  Notificação  │ (8083)
       │  Service      │
       └───────┬───────┘
               │
               v
       ┌───────────────┐
       │  Usuários     │ (8081)
       │  Service      │
       └───────────────┘
```

---

## 📋 Comandos Úteis

### Verificar Status dos Containers
```bash
docker-compose ps
```

### Ver Logs de Todos os Serviços
```bash
docker-compose logs -f
```

### Ver Logs de um Serviço Específico
```bash
docker-compose logs -f usuarios-service
docker-compose logs -f pagamento-service
docker-compose logs -f notification-service
docker-compose logs -f catalogo-service
```

### Reiniciar Serviços
```bash
# Reiniciar todos
docker-compose restart

# Reiniciar um serviço específico
docker-compose restart usuarios-service
```

### Parar Serviços
```bash
docker-compose down
```

### Parar e Remover Volumes
```bash
docker-compose down -v
```

### Rebuild e Start
```bash
docker-compose up --build -d
```

---

## 🧪 Testando a Aplicação

### 1. Criar um Usuário
```bash
curl -X POST http://localhost:8081/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "senha123"
  }'
```

### 2. Cadastrar um Livro
```bash
curl -X POST http://localhost:8084/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "isbn": "9780132350884",
    "preco": 89.90
  }'
```

### 3. Realizar uma Compra
```bash
curl -X POST http://localhost:8084/compras \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "livroId": 1
  }'
```

### 4. Verificar Notificações
```bash
curl http://localhost:8083/notificacoes
```

---

## 🔧 Configurações de Ambiente

### Variáveis de Ambiente Configuradas

**MySQL Database:**
- `MYSQL_ROOT_PASSWORD`: root
- `MYSQL_DATABASE`: microservicos

**Todos os Serviços:**
- `SPRING_DATASOURCE_URL`: jdbc:mysql://db:3306/{database_name}
- `SPRING_DATASOURCE_USERNAME`: root
- `SPRING_DATASOURCE_PASSWORD`: root
- URLs dos outros serviços para comunicação REST

---

## 📊 Healthchecks

### MySQL
- **Comando:** `mysqladmin ping -h localhost -u root -proot`
- **Intervalo:** 10s
- **Timeout:** 5s
- **Retries:** 5

### Serviços Spring Boot
Os serviços aguardam o MySQL estar saudável antes de iniciar através da configuração `depends_on` com `condition: service_healthy`.

---

## 🌐 Rede Docker

**Nome da Rede:** `microservices-network`

Todos os serviços estão na mesma rede Docker, permitindo comunicação entre eles usando os nomes dos serviços como hostnames.

---

## 💾 Volumes

**Volume do MySQL:** `db_data`
- Persiste os dados do banco de dados
- Localização: Gerenciado pelo Docker

**Script de Inicialização:**
- `init.sql` é executado automaticamente na primeira inicialização do MySQL
- Cria as 4 databases necessárias

---

## ⚠️ Troubleshooting

### Porta 3306 já em uso
**Problema:** Erro "bind: address already in use" na porta 3306  
**Solução:** Alterada a porta externa do MySQL para 3307

### Serviço não inicia
**Verificar logs:**
```bash
docker-compose logs [nome-do-serviço]
```

### Conectividade entre serviços
**Verificar rede:**
```bash
docker network inspect trabalhofinal-av3_microservices-network
```

### Recompilar serviços
```bash
cd [diretório-do-serviço]
./mvnw.cmd clean package -DskipTests
cd ../..
docker-compose up --build -d
```

---

## 📝 Notas Importantes

1. **Ordem de Inicialização:** O MySQL inicia primeiro e só depois os serviços Java devido ao healthcheck
2. **Comunicação REST:** Todos os serviços se comunicam via REST (RabbitMQ foi removido)
3. **Porta MySQL:** A porta externa foi alterada para 3307 para evitar conflito com instâncias locais
4. **CORS:** Todos os controllers têm `@CrossOrigin(origins = "*")` configurado
5. **Logs:** Os logs são verbosos durante a inicialização, isso é normal

---

## 🎯 Próximos Passos

- [ ] Adicionar autenticação JWT
- [ ] Implementar circuit breaker (Resilience4j)
- [ ] Adicionar monitoring (Prometheus/Grafana)
- [ ] Implementar API Gateway (Spring Cloud Gateway)
- [ ] Adicionar testes de integração
- [ ] Configurar CI/CD pipeline

---

## 📚 Documentação Completa da API

Consulte o arquivo `README.md` para documentação detalhada de todos os endpoints.

---

**Desenvolvido por:** Projeto AV3 - Unifor  
**Última atualização:** 06/12/2025
