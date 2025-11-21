// hooks/useAuth.jsx
import { useState, useEffect, createContext, useContext } from "react";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userEmail, setUserEmail] = useState("");
  const [userRole, setUserRole] = useState("");
  const [userId, setUserId] = useState("");
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);

  // Cargar sesión y datos de usuario
  useEffect(() => {
    const token = localStorage.getItem("token");
    const email = localStorage.getItem("userEmail");
    const role = localStorage.getItem("userRole");
    const id = localStorage.getItem("userId");

    if (token && email && id) {
      setIsAuthenticated(true);
      setUserEmail(email);
      setUserRole(role || "");
      setUserId(id);
      fetchUserData(token, id);
    } else {
      setIsAuthenticated(false);
    }
    setLoading(false);
  }, []);

  // 🔹 Obtener datos reales del usuario
  const fetchUserData = async (token, id) => {
    try {
      const endpoint = `http://localhost:8181/api/petconnect/usuarios/${id}`;
      const res = await fetch(endpoint, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Error obteniendo datos del usuario");
      const data = await res.json();

      // Si el backend devuelve el rol, lo guardamos
      if (data.role && data.role !== userRole) {
        setUserRole(data.role);
        localStorage.setItem("userRole", data.role);
      }

      setUserData(data);
    } catch (error) {
      console.error("⚠️ Error cargando datos del usuario:", error);
    }
  };

  // 🔹 Login: guarda datos y obtiene el perfil real
  const login = (token, email, role, id) => {
    console.log("🔑 Iniciando sesión:", { email, role, id });

    if (!role) console.warn("⚠️ El backend no envió el rol del usuario.");

    localStorage.setItem("token", token);
    localStorage.setItem("userEmail", email);
    if (role) localStorage.setItem("userRole", role);
    if (id) localStorage.setItem("userId", id.toString());

    setIsAuthenticated(true);
    setUserEmail(email);
    setUserRole(role || "");
    setUserId(id ? id.toString() : "");

    fetchUserData(token, id);
    setLoading(false);
  };

  // 🔹 Logout
  const logout = () => {
    console.log("🚪 Cerrando sesión");
    localStorage.removeItem("token");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userRole");
    localStorage.removeItem("userId");

    setIsAuthenticated(false);
    setUserEmail("");
    setUserRole("");
    setUserId("");
    setUserData(null);
  };

  // 🔹 Actualizar datos (por ejemplo, al editar perfil)
  const updateUserData = (newData) => {
    setUserData((prev) => ({ ...prev, ...newData }));
  };

  const value = {
    isAuthenticated,
    userEmail,
    userRole,
    userId,
    userData,
    loading,
    login,
    logout,
    updateUserData,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
