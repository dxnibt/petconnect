import { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../styles/home.css";
import { useAuth } from "../../hooks/useAuth.jsx";
import AdoptionModal from "../components/AdoptionModal.jsx";


export default function Home() {
  const [mascotas, setMascotas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeSection, setActiveSection] = useState("inicio");
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize] = useState(6);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedMascota, setSelectedMascota] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const navigate = useNavigate();
  const { isAuthenticated, userEmail, userRole, logout, userId } = useAuth();

  useEffect(() => {
    fetchMascotas(currentPage);
  }, [isAuthenticated, currentPage]);

  const fetchMascotas = async (page = 0) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:9494/api/petconnect/mascotas/List?page=${page}&size=${pageSize}`);
      
      if (Array.isArray(response.data)) {
        setMascotas(response.data);
        
        if (response.data.length < pageSize) {
          setTotalPages(page + 1);
        } else {
          setTotalPages(page + 2);
        }
      } else {
        setMascotas([]);
        setTotalPages(0);
      }
    } catch (error) {
      console.error("Error al cargar mascotas:", error);
      setMascotas([]);
      setTotalPages(0);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
    }
  };

  const handleDeleteMascota = async (mascotaId) => {
    if (!mascotaId) {
      alert("Error: ID de mascota no válido");
      return;
    }

    if (!window.confirm("¿Estás seguro de que quieres eliminar esta mascota?")) {
      return;
    }

    try {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("No estás autenticado. Inicia sesión primero.");
        return;
      }

      await axios.delete(`http://localhost:9494/api/petconnect/mascotas/delete/${mascotaId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      
      alert("Mascota eliminada exitosamente");
      fetchMascotas(currentPage);
    } catch (error) {
      console.error("Error al eliminar mascota:", error);
      alert("Error al eliminar la mascota: " + (error.response?.data?.message || error.message));
    }
  };

  const handleAdopt = (mascota) => {
    if (!isAuthenticated) {
      alert("Debes iniciar sesión para adoptar una mascota");
      navigate("/login");
      return;
    }
    
    if (userRole !== "ADOPTANTE") {
      alert(`Los usuarios con rol ${userRole} no pueden adoptar mascotas. Solo los adoptantes pueden realizar adopciones.`);
      return;
    }
    
    setSelectedMascota(mascota);
    setIsModalOpen(true);
  };

  const handleAdoptionSuccess = () => {
    console.log("Adopción exitosa!");
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedMascota(null);
  };

  const scrollToSection = (sectionId) => {
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: "smooth" });
    }
    setActiveSection(sectionId);
  };

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  // Funciones para verificar permisos de edición/eliminación - CORREGIDAS
  const puedeEditarMascota = (mascota) => {
    if (!isAuthenticated) return false;
    if (userRole === "ADMIN") return true;
    if (userRole === "REFUGIO") {
      return mascota.shelter_Id == userId;
    }
    return false;
  };

  const puedeEliminarMascota = (mascota) => {
    if (!isAuthenticated) return false;
    if (userRole === "ADMIN") return true;
    if (userRole === "REFUGIO") {
      return mascota.shelter_Id == userId;
    }
    return false;
  };

  // Permisos generales
  const canCreatePets = isAuthenticated && (userRole === "ADMIN" || userRole === "REFUGIO");
  const canAdoptPets = isAuthenticated && userRole === "ADOPTANTE";

  const Pagination = () => {
    if (totalPages <= 1) return null;

    return (
      <div className="pagination-container">
        <div className="pagination">
          <button
            className={`pagination-btn ${currentPage === 0 ? 'disabled' : ''}`}
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 0}
          >
            ← Anterior
          </button>

          <div className="page-numbers">
            {Array.from({ length: totalPages }, (_, index) => (
              <button
                key={index}
                className={`page-number ${currentPage === index ? 'active' : ''}`}
                onClick={() => handlePageChange(index)}
              >
                {index + 1}
              </button>
            ))}
          </div>

          <button
            className={`pagination-btn ${currentPage === totalPages - 1 ? 'disabled' : ''}`}
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages - 1}
          >
            Siguiente →
          </button>
        </div>
        
        <div className="page-info">
          Página {currentPage + 1} de {totalPages} • 
          Mostrando {mascotas.length} mascotas
        </div>
      </div>
    );
  };

  return (
    <div className="home-container">
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
                
                <Link to="/profile">
                  <button className="auth-btn profile-btn">
                    👤 Mi Perfil
                  </button>
                </Link>

                {canCreatePets && (
                  <Link to="/crear">
                    <button className="auth-btn create-pet-btn">
                      🐾 Crear Mascota
                    </button>
                  </Link>
                )}
                
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

        <section id="mascotas" className="pets-section">
          <div className="section-header">
            <h2>Mascotas Disponibles</h2>
            <p>Conoce a nuestros compañeros que buscan un hogar - Página {currentPage + 1}</p>
            
            {canCreatePets && (
              <div className="create-pet-section">
                <Link to="/crear">
                  <button className="create-pet-main-btn">
                    ＋ Agregar Nueva Mascota
                  </button>
                </Link>
              </div>
            )}
          </div>

          {loading ? (
            <div className="loading-container">
              <div className="loading-spinner"></div>
              <p>Cargando mascotas...</p>
            </div>
          ) : (
            <>
              <div className="mascotas-grid">
                {mascotas.length > 0 ? (
                  mascotas.map((mascota, index) => {
                    const mascotaId = mascota.pet_id || mascota.id || mascota.mascotaId;
                    const mascotaName = mascota.name || mascota.nombre || "Sin nombre";
                    
                    return (
                      <div key={mascotaId || index} className="mascota-card">
                        <div className="card-image">
                          <img
                            src={
                              mascota.imageUrl || 
                              mascota.imagenUrl ||
                              "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&h=200&fit=crop"
                            }
                            alt={mascotaName}
                            onError={(e) => {
                              e.target.src = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&h=200&fit=crop";
                            }}
                          />
                          
                          <div className="card-actions">
                            {puedeEditarMascota(mascota) && (
                              <Link to={`/editar/${mascotaId}`}>
                                <button 
                                  className="action-btn edit-btn"
                                  title="Editar mascota"
                                >
                                  ✏️
                                </button>
                              </Link>
                            )}
                            
                            {puedeEliminarMascota(mascota) && (
                              <button 
                                className="action-btn delete-btn"
                                onClick={() => handleDeleteMascota(mascotaId)}
                                title="Eliminar mascota"
                              >
                                🗑️
                              </button>
                            )}
                          </div>
                          
                          {canAdoptPets && (
                            <div className="card-overlay">
                              <button 
                                className="adopt-btn"
                                onClick={() => handleAdopt(mascota)}
                              >
                                ¡Adóptame!
                              </button>
                            </div>
                          )}
                        </div>
                        <div className="card-content">
                          <h3>{mascotaName}</h3>
                          
                          {isAuthenticated && userRole === "REFUGIO" && (
                            <div className="pet-ownership-indicator">
                              {mascota.shelter_Id == userId ? (
                                <span className="owned-badge">✅ Tu mascota</span>
                              ) : (
                                <span className="not-owned-badge">🔒 Mascota de otro refugio</span>
                              )}
                            </div>
                          )}
                          
                          <p className="pet-species">
                            {mascota.species === 'PERRO' ? '🐕 Perro' : 
                             mascota.species === 'GATO' ? '🐈 Gato' : 
                             '🐾 ' + (mascota.otherspecies || mascota.especie || 'Otra especie')}
                            {mascota.race && ` • ${mascota.race}`}
                            {mascota.raza && ` • ${mascota.raza}`}
                          </p>
                          <p className="pet-description">
                            {mascota.description || mascota.descripcion || "Esta mascota está buscando un hogar lleno de amor y cuidados."}
                          </p>
                          <div className="pet-tags">
                            {mascota.childFriendly && <span className="tag">👶 Amigable con niños</span>}
                            {mascota.sterilization && <span className="tag">✂️ Esterilizado</span>}
                            <span className="tag">❤️ Necesita Hogar</span>
                          </div>
                          
                          {isAuthenticated && !canAdoptPets && userRole !== "REFUGIO" && (
                            <div className="adoption-info">
                              <p className="info-text">
                                ⓘ Solo los adoptantes pueden realizar adopciones
                              </p>
                            </div>
                          )}
                          
                          {!isAuthenticated && (
                            <div className="adoption-info">
                              <p className="info-text">
                                ⓘ <Link to="/login">Inicia sesión</Link> como adoptante para adoptar
                              </p>
                            </div>
                          )}
                        </div>
                      </div>
                    );
                  })
                ) : (
                  <div className="no-pets">
                    <div className="no-pets-icon">🐾</div>
                    <h3>No hay mascotas disponibles en esta página</h3>
                    <p>Intenta con otra página o vuelve más tarde.</p>
                    
                    {canCreatePets && (
                      <Link to="/crear">
                        <button className="create-pet-main-btn">
                          ＋ Agregar Nueva Mascota
                        </button>
                      </Link>
                    )}
                  </div>
                )}
              </div>

              <Pagination />

              <div className="show-more-container">
                <Link to="/mascotas">
                  <button className="show-more-btn">
                    Ver catálogo completo de mascotas
                  </button>
                </Link>
              </div>
            </>
          )}
        </section>
      </main>

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

      <AdoptionModal
        mascota={selectedMascota}
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSuccess={handleAdoptionSuccess}
      />
    </div>
  );
}