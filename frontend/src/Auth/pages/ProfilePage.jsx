// src/pages/ProfilePage.jsx
import { useState, useEffect } from "react";
import { useAuth } from "../../hooks/useAuth.jsx";
import axios from "axios";
import { Link } from "react-router-dom";
import "../styles/profile.css";

export default function ProfilePage() {
  const { 
    isAuthenticated, 
    userRole, 
    userData, 
    userId, 
    updateUserData, 
    userEmail, 
    loading: authLoading 
  } = useAuth();

  const [activeTab, setActiveTab] = useState("perfil");
  const [mascotas, setMascotas] = useState([]);
  const [solicitudes, setSolicitudes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState(false);
  const [editForm, setEditForm] = useState({});

  // 🔹 Cargar perfil real al montar
  useEffect(() => {
    if (isAuthenticated && userId) {
      loadUserProfile();
      loadAdditionalData();
    }
  }, [isAuthenticated, userId, userRole]);

  // 🔹 Obtener datos reales del usuario
  const loadUserProfile = async () => {
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get(
        `http://localhost:9494/api/petconnect/usuarios/${userId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      console.log("📥 Datos del usuario:", res.data);

      setEditForm(res.data);
      updateUserData(res.data); // Actualizar el contexto global
    } catch (error) {
      console.error("❌ Error al cargar perfil:", error);
    }
  };

  // 🔹 Cargar datos adicionales según el rol
  const loadAdditionalData = async () => {
    if (!userId) return;
    setLoading(true);

    try {
      const token = localStorage.getItem("token");
      if (userRole === "REFUGIO") {
        const mascotasRes = await axios.get(
          `http://localhost:9494/api/petconnect/mascotas/refugio/${userId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setMascotas(mascotasRes.data || []);
      } else if (userRole === "ADOPTANTE") {
        const solicitudesRes = await axios.get(
          `http://localhost:9494/api/petconnect/adopciones/adoptante/${userId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setSolicitudes(solicitudesRes.data || []);
      }
    } catch (error) {
      console.error("Error cargando datos adicionales:", error);
    } finally {
      setLoading(false);
    }
  };

  // 🔹 Editar perfil
  const handleEdit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      updateUserData(editForm);

      let endpoint = "";
      let dataToSend = {};

      if (userRole === "ADOPTANTE") {
        endpoint = `http://localhost:9494/api/petconnect/adoptantes/update/${userId}`;
        dataToSend = {
          nombre: editForm.nombre || "",
          apellido: editForm.apellido || "",
          telefono: editForm.telefono || "",
          direccion: editForm.direccion || ""
        };
      } else if (userRole === "REFUGIO") {
        endpoint = `http://localhost:9494/api/petconnect/refugios/update/${userId}`;
        dataToSend = {
          nombre: editForm.nombre || "",
          telefono: editForm.telefono || "",
          direccion: editForm.direccion || "",
          descripcion: editForm.descripcion || ""
        };
      }

      if (endpoint) {
        await axios.patch(endpoint, dataToSend, {
          headers: { Authorization: `Bearer ${token}` }
        });
      }

      setEditing(false);
      alert("Perfil actualizado exitosamente!");
    } catch (error) {
      console.error("Error actualizando perfil:", error);
      alert("Error al actualizar el perfil");
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditForm(prev => ({ ...prev, [name]: value }));
  };

  // 🔹 Imagen según rol
  const getProfilePhoto = () => {
    const defaultPhotos = {
      ADOPTANTE: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&h=400&fit=crop&crop=face",
      REFUGIO: "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=400&h=400&fit=crop&crop=center",
      ADMIN: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop=face"
    };
    return defaultPhotos[userRole] || defaultPhotos.ADOPTANTE;
  };

  const getFullName = () => {
    if (userRole === "ADOPTANTE") {
      return `${userData?.name || ""} `.trim() || userEmail?.split("@")[0] || "Usuario";
    }
    return userData?.name || userEmail?.split("@")[0] || "Usuario";
  };

  // 🔹 Validaciones visuales
  if (!isAuthenticated) {
    return (
      <div className="pc-profile-container">
        <div className="pc-not-authenticated">
          <h2>Acceso Denegado</h2>
          <p>Debes iniciar sesión para ver tu perfil.</p>
          <Link to="/login">
            <button className="pc-auth-btn">Iniciar Sesión</button>
          </Link>
        </div>
      </div>
    );
  }

  if (authLoading) {
    return (
      <div className="pc-profile-container">
        <div className="pc-loading">
          <div className="pc-loading-spinner"></div>
          <p>Cargando perfil...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="pc-profile-container">
      <div className="pc-profile-header">
        <h1>Mi Perfil</h1>
        <p>Gestiona tu información y actividades</p>
      </div>

      <div className="pc-profile-layout">
        {/* Columna izquierda - Foto */}
        <div className="pc-profile-sidebar">
          <div className="pc-profile-card">
            <div className="pc-profile-photo-container">
              <img 
                src={getProfilePhoto()} 
                alt="Foto de perfil" 
                className="pc-profile-photo"
              />
            </div>
            <div className="pc-profile-basic-info">
              <h2 className="pc-profile-name">{getFullName()}</h2>
              <p className="pc-profile-email">{userEmail}</p>
              <div className={`pc-role-badge pc-role-${userRole?.toLowerCase()}`}>
                {userRole === "ADOPTANTE" ? "🐾 Adoptante" : userRole === "REFUGIO" ? "🏠 Refugio" : "👑 Administrador"}
              </div>
            </div>
          </div>
        </div>

        {/* Columna derecha */}
        <div className="pc-profile-main">
          <div className="pc-profile-tabs">
            <button 
              className={`pc-tab-btn ${activeTab === "perfil" ? "pc-active" : ""}`}
              onClick={() => setActiveTab("perfil")}
            >
              👤 Información Personal
            </button>
            {userRole === "REFUGIO" && (
              <button 
                className={`pc-tab-btn ${activeTab === "mascotas" ? "pc-active" : ""}`}
                onClick={() => setActiveTab("mascotas")}
              >
                🐕 Mis Mascotas
              </button>
            )}
            {userRole === "ADOPTANTE" && (
              <button 
                className={`pc-tab-btn ${activeTab === "solicitudes" ? "pc-active" : ""}`}
                onClick={() => setActiveTab("solicitudes")}
              >
                📋 Mis Solicitudes
              </button>
            )}
          </div>

          <div className="pc-profile-content">
            {activeTab === "perfil" && (
              <div className="pc-tab-content">
                {!editing ? (
                  <div className="pc-profile-info">
                    <div className="pc-info-section">
                      <h3>Información Personal</h3>
                      <div className="pc-info-grid">
                        <div className="pc-info-item"><label>Nombre completo:</label><span>{getFullName()}</span></div>
                        <div className="pc-info-item"><label>Email:</label><span>{userEmail}</span></div>
                        <div className="pc-info-item"><label>Teléfono:</label><span>{userData?.telefono || "No especificado"}</span></div>
                        <div className="pc-info-item"><label>Dirección:</label><span>{userData?.direccion || "No especificado"}</span></div>
                        {userRole === "REFUGIO" && (
                          <div className="pc-info-item pc-full-width">
                            <label>Descripción del refugio:</label>
                            <span>{userData?.descripcion || "No hay descripción disponible"}</span>
                          </div>
                        )}
                        {userRole === "ADOPTANTE" && (
                          <div className="pc-info-item">
                            <label>Apellido:</label><span>{userData?.apellido || "No especificado"}</span>
                          </div>
                        )}
                      </div>
                    </div>
                    <button className="pc-edit-btn" onClick={() => setEditing(true)}>✏️ Editar Perfil</button>
                  </div>
                ) : (
                  <form className="pc-edit-form" onSubmit={handleEdit}>
                    <h3>Editar Información Personal</h3>
                    <div className="pc-form-grid">
                      <div className="pc-form-group">
                        <label>Nombre *</label>
                        <input type="text" name="nombre" value={editForm.nombre || ""} onChange={handleInputChange} required />
                      </div>
                      {userRole === "ADOPTANTE" && (
                        <div className="pc-form-group">
                          <label>Apellido *</label>
                          <input type="text" name="apellido" value={editForm.apellido || ""} onChange={handleInputChange} required />
                        </div>
                      )}
                      <div className="pc-form-group">
                        <label>Teléfono</label>
                        <input type="tel" name="telefono" value={editForm.telefono || ""} onChange={handleInputChange} />
                      </div>
                      <div className="pc-form-group pc-full-width">
                        <label>Dirección</label>
                        <input type="text" name="direccion" value={editForm.direccion || ""} onChange={handleInputChange} />
                      </div>
                      {userRole === "REFUGIO" && (
                        <div className="pc-form-group pc-full-width">
                          <label>Descripción del refugio</label>
                          <textarea name="descripcion" value={editForm.descripcion || ""} onChange={handleInputChange} rows="4" />
                        </div>
                      )}
                    </div>
                    <div className="pc-form-actions">
                      <button type="button" className="pc-cancel-btn" onClick={() => setEditing(false)}>Cancelar</button>
                      <button type="submit" className="pc-save-btn" disabled={loading}>
                        {loading ? "Guardando..." : "💾 Guardar Cambios"}
                      </button>
                    </div>
                  </form>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
