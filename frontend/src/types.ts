// src/types.ts

export interface User {
  id: number;
  nome: string;
  email: string;
  telefone: string;
}

export interface Book {
  id: number;
  titulo: string;
  autor: string;
  categoria: string;
  preco: number;
}

export interface Notification {
  id: number;
  titulo: string;
  mensagem: string;
  tipo: string;
  dataCriacao: string; // Vem como string ISO do backend Java
  status?: string;
}

export interface PurchasePayload {
  usuarioId: number;
  livroId: number;
  valor: number;
  meioPagamento: string;
}
