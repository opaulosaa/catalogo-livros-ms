-- Script de dados iniciais para teste (Versão Unificada)
USE livraria_db;

-- 1. Dados de Usuários
INSERT INTO usuarios (nome, email, telefone) VALUES
('João Silva', 'joao.silva@email.com', '85988887777'),
('Maria Santos', 'maria.santos@email.com', '85977776666'),
('Pedro Oliveira', 'pedro.oliveira@email.com', '85966665555'),
('Ana Costa', 'ana.costa@email.com', '85955554444'),
('Carlos Souza', 'carlos.souza@email.com', '85944443333');

-- 2. Dados de Livros
INSERT INTO livros (titulo, autor, categoria, preco) VALUES
('Clean Code', 'Robert C. Martin', 'Engenharia de Software', 89.90),
('Design Patterns', 'Gang of Four', 'Engenharia de Software', 95.50),
('Refactoring', 'Martin Fowler', 'Engenharia de Software', 85.00),
('The Pragmatic Programmer', 'Andrew Hunt', 'Desenvolvimento', 92.00),
('Effective Java', 'Joshua Bloch', 'Programação Java', 88.00),
('Domain-Driven Design', 'Eric Evans', 'Arquitetura', 105.00),
('Microservices Patterns', 'Chris Richardson', 'Arquitetura', 98.00),
('Spring in Action', 'Craig Walls', 'Frameworks', 79.90),
('Java Concurrency in Practice', 'Brian Goetz', 'Programação Java', 93.50),
('Building Microservices', 'Sam Newman', 'Arquitetura', 87.00);

-- 3. Dados de Pagamentos
INSERT INTO pagamento (usuario_id, livro_id, valor, status, meio_pagamento, data) VALUES
(1, 1, 89.90, 'APROVED', 'PIX', NOW() - INTERVAL 5 DAY),
(2, 3, 85.00, 'APROVED', 'CARTAO', NOW() - INTERVAL 4 DAY),
(1, 5, 88.00, 'APROVED', 'PIX', NOW() - INTERVAL 3 DAY),
(3, 2, 95.50, 'APROVED', 'BOLETO', NOW() - INTERVAL 2 DAY),
(4, 7, 98.00, 'APROVED', 'CARTAO', NOW() - INTERVAL 1 DAY);

-- 4. Dados de Notificações
INSERT INTO notificacoes (usuario_id, titulo, mensagem, tipo, status, data_criacao, tentativas) VALUES
(1, 'Pagamento Aprovado', 'Seu pagamento para o livro Clean Code no valor de R$ 89,90 foi aprovado!', 'PAGAMENTO', 'ENVIADA', NOW() - INTERVAL 5 DAY, 1),
(2, 'Pagamento Aprovado', 'Seu pagamento para o livro Refactoring no valor de R$ 85,00 foi aprovado!', 'PAGAMENTO', 'ENVIADA', NOW() - INTERVAL 4 DAY, 1),
(1, 'Pagamento Aprovado', 'Seu pagamento para o livro Effective Java no valor de R$ 88,00 foi aprovado!', 'PAGAMENTO', 'ENVIADA', NOW() - INTERVAL 3 DAY, 1),
(3, 'Pagamento Aprovado', 'Seu pagamento para o livro Design Patterns no valor de R$ 95,50 foi aprovado!', 'PAGAMENTO', 'ENVIADA', NOW() - INTERVAL 2 DAY, 1),
(4, 'Pagamento Aprovado', 'Seu pagamento para o livro Microservices Patterns no valor de R$ 98,00 foi aprovado!', 'PAGAMENTO', 'ENVIADA', NOW() - INTERVAL 1 DAY, 1);

COMMIT;