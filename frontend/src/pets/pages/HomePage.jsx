// src/pages/HomePage.jsx
import { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import "../styles/home.css";
import { useAuth } from "../../hooks/useAuth.js";

export default function Home() {
  const [mascotas, setMascotas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeSection, setActiveSection] = useState("inicio");
  
  // 👇 Usar el hook de autenticación
  const { isAuthenticated, userEmail, userRole, logout } = useAuth();

  useEffect(() => {
    const fetchMascotas = async () => {
      try {
        const response = await axios.get("http://localhost:9494/api/petconnect/mascotas/List");
        console.log("✅ API respondió con:", response);
        console.log("✅ Datos:", response.data);

        if (Array.isArray(response.data)) {
          setMascotas(response.data);
        } else {
          console.warn("⚠️ La API no devolvió un array:", response.data);
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

  // 👇 Obtener solo las primeras 3 mascotas
  const mascotasMostradas = mascotas.slice(0, 3);

  const scrollToSection = (sectionId) => {
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: "smooth" });
    }
    setActiveSection(sectionId);
  };

  const handleLogout = () => {
    logout();
    window.location.reload();
  };

  return (
    <div className="home-container">
      {/* Header Mejorado */}
      <header className="main-header">
        <div className="header-content">
          <div className="logo-section">
            <div className="logo">
              <span className="paw">🐾</span>
              <h1>PetConnect</h1>
            </div>
            <nav className="navigation">
              <button 
                className={`nav-btn ${activeSection === 'inicio' ? 'active' : ''}`}
                onClick={() => scrollToSection('inicio')}
              >
                Inicio
              </button>
              <button 
                className={`nav-btn ${activeSection === 'mascotas' ? 'active' : ''}`}
                onClick={() => scrollToSection('mascotas')}
              >
                Mascotas
              </button>
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

      <main>
        {/* Hero Section */}
        <section id="inicio" className="hero-section">
          <div className="hero-content">
            <h2>Conectamos corazones,<br/>encontramos hogares</h2>
            <p>Tu compañero perfecto te está esperando. Descubre mascotas que buscan una segunda oportunidad.</p>
            <button 
              className="cta-button"
              onClick={() => scrollToSection('mascotas')}
            >
              Ver Mascotas Disponibles
            </button>
          </div>
          <div className="hero-image">
            <div className="floating-paws">
              <span>🐾</span>
              <span>🐾</span>
              <span>🐾</span>
            </div>
          </div>
        </section>

        {/* Sección Frase e Imagen */}
        <section id="quienes-somos" className="phrase-hero-section">
          <div className="phrase-hero-image">
            <img 
              src="https://i.pinimg.com/1200x/7a/ac/7e/7aac7eb9f34a54ec650b7dc0523a33f6.jpg" 
              alt="Cachorro y gatito buscando un hogar"
            />
            <div className="phrase-hero-content">
              <div className="phrase-hero-text">
                <h2>
                  Cada mirada merece un hogar,
                  <br />
                  cada corazón un amigo.
                </h2>
              </div>
            </div>
          </div>
        </section>

        {/* Catálogo de Mascotas - MOSTRANDO SOLO 3 */}
        <section id="mascotas" className="pets-section">
          <div className="section-header">
            <h2>Nuestras Mascotas</h2>
            <p>Conoce a estos increíbles compañeros que buscan un hogar</p>
          </div>

          {loading ? (
            <div className="loading-container">
              <div className="loading-spinner"></div>
              <p>Cargando mascotas...</p>
            </div>
          ) : (
            <>
              <div className="mascotas-grid">
                {mascotasMostradas.length > 0 ? (
                  mascotasMostradas.map((mascota, index) => (
                    <div key={mascota.id || index} className="mascota-card">
                      <div className="card-image">
                        <img
                          src={
                            mascota.imageUrl ||
                            "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&h=200&fit=crop"
                          }
                          alt={mascota.name || "Mascota"}
                          onError={(e) => {
                            e.target.src = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&h=200&fit=crop";
                          }}
                        />
                        <div className="card-overlay">
                          <button className="adopt-btn">¡Adóptame!</button>
                        </div>
                      </div>
                      <div className="card-content">
                        <h3>{mascota.name || "Sin nombre"}</h3>
                        <p className="pet-description">
                          {mascota.description || "Esta mascota está buscando un hogar lleno de amor y cuidados."}
                        </p>
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
                    <h3>No hay mascotas disponibles en este momento</h3>
                    <p>Pronto tendremos nuevos amigos esperando por un hogar.</p>
                  </div>
                )}
              </div>

              {/* 👇 Botón "Mostrar más" - Solo aparece si hay más de 3 mascotas */}
              {mascotas.length > 3 && (
                <div className="show-more-container">
                  <Link to="/mascotas">
                    <button className="show-more-btn">
                      Ver todas las mascotas ({mascotas.length})
                    </button>
                  </Link>
                </div>
              )}
            </>
          )}
        </section>
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
          <div className="footer-section">
            <h4>Síguenos</h4>
            <div className="social-links">
              <span>📘</span>
              <span>📷</span>
              <span>🐦</span>
            </div>
          </div>
        </div>
        <div className="footer-bottom">
          <p>&copy; 2024 PetConnect. Todos los derechos reservados.</p>
        </div>
      </footer>
    </div>
  );
}