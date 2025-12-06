-- Script de inicialização dos bancos de dados
-- Este script cria os databases necessários para cada microsserviço

CREATE DATABASE IF NOT EXISTS usuarios_db;
CREATE DATABASE IF NOT EXISTS livros_db;
CREATE DATABASE IF NOT EXISTS pagamento_db;
CREATE DATABASE IF NOT EXISTS notificacao_db;

-- Conceder permissões (opcional, root já tem todas)
GRANT ALL PRIVILEGES ON usuarios_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON livros_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON pagamento_db.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON notificacao_db.* TO 'root'@'%';

FLUSH PRIVILEGES;
