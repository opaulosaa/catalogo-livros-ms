-- Script de inicialização UNIFICADO
-- Cria um único banco de dados para todos os serviços

CREATE DATABASE IF NOT EXISTS livraria_db;

-- Conceder permissões
GRANT ALL PRIVILEGES ON livraria_db.* TO 'root'@'%';

FLUSH PRIVILEGES;