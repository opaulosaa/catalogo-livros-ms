import React, { useState, useEffect } from "react";
import { BookOpen, Users, Bell, ShoppingCart, CheckCircle } from "lucide-react";
import { api } from "./api";
import { type User, type Book, type Notification } from "./types";

// Definição das Abas disponíveis
type TabType = "catalog" | "users" | "notifications";

function App() {
  // Tipagem explícita dos estados
  const [activeTab, setActiveTab] = useState<TabType>("catalog");
  const [users, setUsers] = useState<User[]>([]);
  const [books, setBooks] = useState<Book[]>([]);
  const [notifications, setNotifications] = useState<Notification[]>([]);

  // Pode ser number ou null se nada estiver selecionado
  const [selectedUser, setSelectedUser] = useState<number | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [usersRes, booksRes] = await Promise.allSettled([
        api.getUsers(),
        api.getBooks(),
      ]);

      if (usersRes.status === "fulfilled") {
        setUsers(usersRes.value.data);
        // Seleciona o primeiro usuário automaticamente se houver e nenhum estiver selecionado
        if (usersRes.value.data.length > 0 && selectedUser === null) {
          setSelectedUser(usersRes.value.data[0].id);
        }
      }

      if (booksRes.status === "fulfilled") {
        setBooks(booksRes.value.data);
      }
    } catch (error) {
      console.error("Erro ao carregar dados:", error);
    }
    setLoading(false);
  };

  useEffect(() => {
    (async () => {
      await fetchData();
    })();
  }, []);

  const handleBuy = async (bookId: number, price: number) => {
    if (selectedUser === null)
      return alert("Selecione um usuário no menu lateral primeiro!");

    if (!window.confirm(`Comprar livro por R$ ${price.toFixed(2)}?`)) return;

    try {
      await api.buyBook({
        usuarioId: selectedUser,
        livroId: bookId,
        valor: price,
        meioPagamento: "PIX",
      });
      alert("Compra realizada! Verifique a aba Notificações.");
      fetchNotifications();
    } catch (error) {
      console.error(error);
      alert(
        "Erro ao processar compra. Verifique se os serviços estão rodando."
      );
    }
  };

  const fetchNotifications = async () => {
    if (selectedUser === null) return;
    try {
      const res = await api.getUserNotifications(selectedUser);
      setNotifications(res.data);
    } catch (e) {
      console.error("Erro ao buscar notificações");
    }
  };

  // Handler tipado para o select
  const handleUserChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const userId = Number(e.target.value);
    setSelectedUser(userId);
    setNotifications([]); // Limpa notificações ao trocar de usuário
  };

  return (
    <div className="min-h-screen flex bg-slate-50 text-slate-800 font-sans">
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-slate-200 p-6 flex flex-col fixed h-full shadow-sm z-10">
        <h1 className="text-2xl font-bold text-slate-800 mb-10 flex items-center gap-2">
          <BookOpen className="w-8 h-8" /> Livraria MS
        </h1>

        <nav className="space-y-2 flex-1">
          <TabButton
            icon={<BookOpen size={20} />}
            label="Catálogo"
            active={activeTab === "catalog"}
            onClick={() => setActiveTab("catalog")}
          />
          <TabButton
            icon={<Users size={20} />}
            label="Usuários"
            active={activeTab === "users"}
            onClick={() => setActiveTab("users")}
          />
          <TabButton
            icon={<Bell size={20} />}
            label="Notificações"
            active={activeTab === "notifications"}
            onClick={() => {
              setActiveTab("notifications");
              fetchNotifications();
            }}
          />
        </nav>

        <div className="mt-auto pt-6 border-t border-slate-100">
          <label className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
            Usuário Logado
          </label>
          <select
            className="w-full mt-2 p-2.5 border border-slate-300 rounded-md text-sm bg-slate-50 focus:ring-2 focus:ring-slate-500 outline-none"
            onChange={handleUserChange}
            value={selectedUser ?? ""}
          >
            {users.length === 0 && <option value="">Carregando...</option>}
            {users.map((u) => (
              <option key={u.id} value={u.id}>
                {u.nome}
              </option>
            ))}
          </select>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 ml-64 p-10 overflow-y-auto min-h-screen">
        <header className="mb-8 flex justify-between items-center pb-6 border-b border-slate-200">
          <div>
            <h2 className="text-3xl font-light text-slate-800">
              {activeTab === "catalog" && "Livros Disponíveis"}
              {activeTab === "users" && "Base de Usuários"}
              {activeTab === "notifications" && "Minhas Notificações"}
            </h2>
            <p className="text-slate-500 text-sm mt-1">
              Gerenciamento via Microsserviços
            </p>
          </div>
          <button
            onClick={fetchData}
            className="px-4 py-2 bg-white border border-slate-300 rounded-lg text-sm font-medium hover:bg-slate-50 transition-colors cursor-pointer"
          >
            Atualizar Dados
          </button>
        </header>

        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 opacity-50">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-slate-600 mb-4"></div>
            <p>Conectando aos microsserviços...</p>
          </div>
        ) : (
          <div className="animate-fade-in">
            {/* CATÁLOGO VIEW */}
            {activeTab === "catalog" && (
              <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
                {books.length === 0 && (
                  <p className="text-slate-500 col-span-3 text-center py-10">
                    Nenhum livro encontrado ou serviço offline.
                  </p>
                )}
                {books.map((book) => (
                  <div
                    key={book.id}
                    className="bg-white p-6 rounded-xl shadow-sm border border-slate-200 hover:shadow-lg hover:-translate-y-1 transition-all duration-200"
                  >
                    <div className="h-40 bg-gradient-to-br from-slate-100 to-slate-200 rounded-lg mb-4 flex items-center justify-center text-slate-400">
                      <BookOpen size={48} strokeWidth={1.5} />
                    </div>
                    <div className="mb-4">
                      <h3 className="font-bold text-lg text-slate-800 leading-tight mb-1">
                        {book.titulo}
                      </h3>
                      <p className="text-sm text-slate-500 font-medium">
                        {book.autor}
                      </p>
                      <span className="inline-block mt-2 px-2 py-1 bg-slate-50 text-slate-700 text-xs rounded-md font-semibold">
                        {book.categoria}
                      </span>
                    </div>
                    <div className="flex justify-between items-center pt-4 border-t border-slate-100">
                      <span className="text-2xl font-bold text-slate-900">
                        R$ {book.preco.toFixed(2)}
                      </span>
                      <button
                        onClick={() => handleBuy(book.id, book.preco)}
                        className="bg-slate-600 text-white px-4 py-2 rounded-lg text-sm font-medium flex items-center gap-2 hover:bg-slate-700 active:bg-slate-800 transition-colors shadow-md shadow-slate-200 cursor-pointer"
                      >
                        <ShoppingCart size={16} /> Comprar
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* NOTIFICATIONS VIEW */}
            {activeTab === "notifications" && (
              <div className="max-w-3xl space-y-4">
                {notifications.length === 0 && (
                  <div className="text-center py-16 bg-white rounded-xl border border-dashed border-slate-300">
                    <Bell className="mx-auto h-12 w-12 text-slate-300 mb-3" />
                    <p className="text-slate-500">
                      Nenhuma notificação encontrada para este usuário.
                    </p>
                  </div>
                )}
                {notifications.map((notif) => (
                  <div
                    key={notif.id}
                    className="bg-white p-5 rounded-lg border-l-4 border-green-500 shadow-sm flex gap-4 transition-all hover:shadow-md"
                  >
                    <div className="mt-1 bg-green-100 p-2 rounded-full h-fit text-green-600">
                      <CheckCircle size={20} />
                    </div>
                    <div>
                      <h4 className="font-bold text-slate-800 text-lg">
                        {notif.titulo}
                      </h4>
                      <p className="text-slate-600 mt-1 leading-relaxed">
                        {notif.mensagem}
                      </p>
                      <span className="text-xs text-slate-400 mt-3 block font-medium uppercase tracking-wide">
                        {new Date(notif.dataCriacao).toLocaleString()} •{" "}
                        {notif.tipo}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}

            {/* USERS VIEW */}
            {activeTab === "users" && (
              <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
                <table className="w-full text-left border-collapse">
                  <thead className="bg-slate-50 border-b border-slate-200">
                    <tr>
                      <th className="p-4 text-xs font-bold text-slate-500 uppercase tracking-wider">
                        ID
                      </th>
                      <th className="p-4 text-xs font-bold text-slate-500 uppercase tracking-wider">
                        Nome
                      </th>
                      <th className="p-4 text-xs font-bold text-slate-500 uppercase tracking-wider">
                        Email
                      </th>
                      <th className="p-4 text-xs font-bold text-slate-500 uppercase tracking-wider">
                        Telefone
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((user) => (
                      <tr
                        key={user.id}
                        className="border-b border-slate-100 hover:bg-slate-50/50 transition-colors last:border-0"
                      >
                        <td className="p-4 text-slate-500 font-mono text-sm">
                          #{user.id}
                        </td>
                        <td className="p-4 font-semibold text-slate-700">
                          {user.nome}
                        </td>
                        <td className="p-4 text-slate-600">{user.email}</td>
                        <td className="p-4 text-slate-500 text-sm">
                          {user.telefone}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}
      </main>
    </div>
  );
}

// Interface para as Props do componente TabButton
interface TabButtonProps {
  icon: React.ReactNode;
  label: string;
  active: boolean;
  onClick: () => void;
}

function TabButton({ icon, label, active, onClick }: TabButtonProps) {
  return (
    <button
      onClick={onClick}
      className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 font-medium text-sm cursor-pointer ${
        active
          ? "bg-slate-50 text-slate-700 shadow-sm"
          : "text-slate-600 hover:bg-slate-50 hover:text-slate-900"
      }`}
    >
      {icon} {label}
    </button>
  );
}

export default App;
