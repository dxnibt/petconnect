// src/pages/MascotasPage.jsx
import { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import "../styles/mascotas.css";
import { useAuth } from "../../hooks/useAuth.js";

export default function MascotasPage() {
  const [mascotas, setMascotas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("todas");

  const { isAuthenticated, userEmail, userRole, logout } = useAuth();

  useEffect(() => {
    const fetchMascotas = async () => {
      try {
        const response = await axios.get("http://localhost:9494/api/petconnect/mascotas/List");
        if (Array.isArray(response.data)) {
          setMascotas(response.data);
        } else {
          setMascotas([]);
        }
      } catch (error) {
        console.error("❌ Error al cargar mascotas:", error);
        setMascotas([]);
      } finally {
        setLoading(false);
      }
    };

    fetchMascotas();
  }, []);

  const handleLogout = () => {
    logout();
    window.location.href = "/";
  };

  // Filtrar mascotas si es necesario
  const mascotasFiltradas = filter === "todas" 
    ? mascotas 
    : mascotas.filter(mascota => 
        mascota.type?.toLowerCase() === filter.toLowerCase() ||
        mascota.tipo?.toLowerCase() === filter.toLowerCase()
      );

  return (
    <div className="mascotas-container">
      {/* Header */}
      <header className="main-header">
        <div className="header-content">
          <div className="logo-section">
            <Link to="/" className="logo">
              <span className="paw">🐾</span>
              <h1>PetConnect</h1>
            </Link>
            <nav className="navigation">
              <Link to="/" className="nav-link">Inicio</Link>
              <span className="nav-link active">Todas las Mascotas</span>
            </nav>
          </div>
          
          <div className="auth-buttons">
            {!isAuthenticated ? (
              <>
                <Link to="/register">
                  <button className="auth-btn signin-btn">Iniciar Sesión</button>
                </Link>
                <Link to="/register">
                  <button className="auth-btn signup-btn">Registrarse</button>
                </Link>
              </>
            ) : (
              <div className="user-menu">
                <span className="user-info">
                  ¡Hola, {userEmail}!
                  {userRole && <span className="user-role">({userRole})</span>}
                </span>
                <button 
                  className="auth-btn logout-btn"
                  onClick={handleLogout}
                >
                  Cerrar Sesión
                </button>
              </div>
            )}
          </div>
        </div>
      </header>

      <main className="mascotas-main">
        {/* Header de la página de mascotas */}
        <div className="mascotas-header">
          <h1>Todas Nuestras Mascotas</h1>
          <p>Encuentra a tu compañero perfecto entre todos nuestros amigos disponibles</p>
          
          {/* Filtros opcionales */}
          <div className="filters">
            <button 
              className={`filter-btn ${filter === 'todas' ? 'active' : ''}`}
              onClick={() => setFilter('todas')}
            >
              Todas
            </button>
            <button 
              className={`filter-btn ${filter === 'perro' ? 'active' : ''}`}
              onClick={() => setFilter('perro')}
            >
              Perros
            </button>
            <button 
              className={`filter-btn ${filter === 'gato' ? 'active' : ''}`}
              onClick={() => setFilter('gato')}
            >
              Gatos
            </button>
          </div>
        </div>

        {/* Grid de todas las mascotas */}
        {loading ? (
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Cargando todas las mascotas...</p>
          </div>
        ) : (
          <div className="mascotas-grid-full">
            {mascotasFiltradas.length > 0 ? (
              mascotasFiltradas.map((mascota, index) => (
                <div key={mascota.id || index} className="mascota-card-large">
                  <div className="card-image">
                    <img
                      src={
                        mascota.imageUrl ||
                        "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=400&h=300&fit=crop"
                      }
                      alt={mascota.name || "Mascota"}
                      onError={(e) => {
                        e.target.src = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=400&h=300&fit=crop";
                      }}
                    />
                    <div className="card-overlay">
                      <button className="adopt-btn-large">¡Quiero Adoptar!</button>
                    </div>
                  </div>
                  <div className="card-content">
                    <h3>{mascota.name || "Sin nombre"}</h3>
                    <p className="pet-description">
                      {mascota.description || "Esta mascota está buscando un hogar lleno de amor y cuidados."}
                    </p>
                    <div className="pet-details">
                      {mascota.age && <span>Edad: {mascota.age}</span>}
                      {mascota.breed && <span>Raza: {mascota.breed}</span>}
                      {mascota.size && <span>Tamaño: {mascota.size}</span>}
                    </div>
                    <div className="pet-tags">
                      <span className="tag">🐕 Mascota</span>
                      <span className="tag">❤️ Necesita Hogar</span>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="no-pets">
                <div className="no-pets-icon">🐾</div>
                <h3>No hay mascotas disponibles</h3>
                <p>Pronto tendremos nuevos amigos esperando por un hogar.</p>
              </div>
            )}
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="main-footer">
        <div className="footer-content">
          <div className="footer-section">
            <div className="logo">
              <span className="paw">🐾</span>
              <h3>PetConnect</h3>
            </div>
            <p>Conectando mascotas con hogares desde 2024</p>
          </div>
          <div className="footer-section">
            <h4>Contacto</h4>
            <p>info@petconnect.com</p>
            <p>+1 234 567 890</p>
          </div>
        </div>
      </footer>
    </div>
  );
}