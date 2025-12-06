import axios, { type AxiosResponse } from "axios";
import { type User, type Book, type Notification, type PurchasePayload } from "./types";

// Definição das URLs (Ambiente ou Fallback)
const URLS = {
  USERS: import.meta.env.VITE_API_USERS || "http://localhost:8081",
  CATALOG: import.meta.env.VITE_API_CATALOG || "http://localhost:8084",
  PAYMENT: import.meta.env.VITE_API_PAYMENT || "http://localhost:8082",
  NOTIFICATION:
    import.meta.env.VITE_API_NOTIFICATION || "http://localhost:8083",
};

export const api = {
  // Usuários
  createUser: (data: Omit<User, "id">): Promise<AxiosResponse<User>> =>
    axios.post(`${URLS.USERS}/user`, data),

  getUsers: (): Promise<AxiosResponse<User[]>> =>
    axios.get(`${URLS.USERS}/user`),

  // Catálogo
  getBooks: (): Promise<AxiosResponse<Book[]>> =>
    axios.get(`${URLS.CATALOG}/livros`),

  createBook: (data: Omit<Book, "id">): Promise<AxiosResponse<Book>> =>
    axios.post(`${URLS.CATALOG}/livros`, data),

  buyBook: (data: PurchasePayload): Promise<AxiosResponse<any>> =>
    axios.post(`${URLS.CATALOG}/compras`, data),

  // Notificações
  getUserNotifications: (
    userId: number
  ): Promise<AxiosResponse<Notification[]>> =>
    axios.get(`${URLS.NOTIFICATION}/notificacoes/usuario/${userId}`),
};
