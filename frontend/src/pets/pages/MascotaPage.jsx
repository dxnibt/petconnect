// src/pets/pages/AllPetsPage.jsx
import { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../styles/home.css";
import { useAuth } from "../../hooks/useAuth.jsx";
import AdoptionModal from "../components/AdoptionModal.jsx";

export default function MascotaPage() {
  const [mascotas, setMascotas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState("all");
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize] = useState(9);
  
  // Estados para el modal de adopción
  const [selectedMascota, setSelectedMascota] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  
  const navigate = useNavigate();
  const { isAuthenticated, userRole, userId } = useAuth();

  useEffect(() => {
    fetchAllMascotas();
  }, []);

  const fetchAllMascotas = async () => {
    try {
      setLoading(true);
      const response = await axios.get("http://localhost:9494/api/petconnect/mascotas/List?page=-1&size=1000");
      
      if (Array.isArray(response.data)) {
        const mascotasOrdenadas = response.data.sort((a, b) => {
          const idA = a.pet_id || a.id || 0;
          const idB = b.pet_id || b.id || 0;
          return idB - idA;
        });
        setMascotas(mascotasOrdenadas);
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

  // Filtrar y buscar mascotas
  const filteredMascotas = mascotas.filter(mascota => {
    const typeMatch = filter === "all" || 
                     (filter === "dogs" && mascota.species === 'PERRO') ||
                     (filter === "cats" && mascota.species === 'GATO') ||
                     (filter === "other" && mascota.species !== 'PERRO' && mascota.species !== 'GATO');
    
    const searchMatch = !searchTerm || 
                       (mascota.name && mascota.name.toLowerCase().includes(searchTerm.toLowerCase())) ||
                       (mascota.nombre && mascota.nombre.toLowerCase().includes(searchTerm.toLowerCase()));
    
    return typeMatch && searchMatch;
  });

  // Paginación
  const totalPages = Math.ceil(filteredMascotas.length / pageSize);
  const startIndex = currentPage * pageSize;
  const currentMascotas = filteredMascotas.slice(startIndex, startIndex + pageSize);

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setCurrentPage(newPage);
      const scrollContainer = document.querySelector('.scrollable-content');
      if (scrollContainer) {
        scrollContainer.scrollTo({ top: 0, behavior: 'smooth' });
      }
    }
  };

  // Función para manejar la adopción
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

  // Función cuando la adopción es exitosa
  const handleAdoptionSuccess = () => {
    console.log("Adopción exitosa!");
  };

  // Función para cerrar el modal
  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedMascota(null);
  };

  // Verificar permisos
  const canAdoptPets = isAuthenticated && userRole === "ADOPTANTE";

  // Componente de Paginación
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
          Mostrando {currentMascotas.length} de {filteredMascotas.length} mascotas
        </div>
      </div>
    );
  };

  return (
    <div className="home-container all-pets-page">
      {/* Header Fijo */}
      <header className="main-header">
        <div className="header-content">
          <div className="logo-section">
            <Link to="/" className="logo" style={{ textDecoration: 'none' }}>
              <span className="paw">🐾</span>
              <h1>PetConnect</h1>
            </Link>
          </div>
          
          <div className="auth-buttons">
            <Link to="/">
              <button className="auth-btn signin-btn" style={{ background: 'transparent', color: '#FF6B6B', border: '2px solid #FF6B6B' }}>
                ← Volver al Inicio
              </button>
            </Link>
          </div>
        </div>
      </header>

      {/* Contenido con Scroll Interno */}
      <div className="scrollable-content">
        <main>
          {/* Hero Section del Catálogo - SUPER COMPACTO */}
          <section className="catalog-hero" style={{ 
            padding: '30px 2rem 15px', 
            minHeight: 'auto'
          }}>
            <div className="catalog-hero-content">
              <h1 style={{ fontSize: '1.5rem', marginBottom: '0.3rem' }}>Catálogo Completo</h1>
              <p style={{ fontSize: '0.9rem', marginBottom: '1rem' }}>Encuentra a tu compañero ideal</p>
              <div className="hero-stats" style={{ gap: '1.5rem' }}>
                <div className="stat">
                  <span className="stat-number" style={{ fontSize: '1.2rem' }}>{mascotas.length}</span>
                  <span className="stat-label" style={{ fontSize: '0.7rem' }}>Total</span>
                </div>
                <div className="stat">
                  <span className="stat-number" style={{ fontSize: '1.2rem' }}>{mascotas.filter(m => m.species === 'PERRO').length}</span>
                  <span className="stat-label" style={{ fontSize: '0.7rem' }}>Perros</span>
                </div>
                <div className="stat">
                  <span className="stat-number" style={{ fontSize: '1.2rem' }}>{mascotas.filter(m => m.species === 'GATO').length}</span>
                  <span className="stat-label" style={{ fontSize: '0.7rem' }}>Gatos</span>
                </div>
              </div>
            </div>
          </section>

          {/* Sección de Filtros y Búsqueda - SUPER COMPACTO */}
          <section className="filters-section" style={{ 
            padding: '0.8rem 1rem',
            marginBottom: '0.5rem'
          }}>
            <div className="filters-container" style={{ gap: '0.5rem' }}>
              
              

              {/* Filtros */}
              <div className="filters-group" style={{ gap: '0.3rem' }}>
                <button 
                  className={`filter-btn ${filter === "all" ? "active" : ""}`}
                  onClick={() => {
                    setFilter("all");
                    setCurrentPage(0);
                  }}
                  style={{
                    padding: '0.4rem 0.8rem',
                    fontSize: '0.8rem'
                  }}
                >
                  Todas
                </button>
                <button 
                  className={`filter-btn ${filter === "dogs" ? "active" : ""}`}
                  onClick={() => {
                    setFilter("dogs");
                    setCurrentPage(0);
                  }}
                  style={{
                    padding: '0.4rem 0.8rem',
                    fontSize: '0.8rem'
                  }}
                >
                  🐕
                </button>
                <button 
                  className={`filter-btn ${filter === "cats" ? "active" : ""}`}
                  onClick={() => {
                    setFilter("cats");
                    setCurrentPage(0);
                  }}
                  style={{
                    padding: '0.4rem 0.8rem',
                    fontSize: '0.8rem'
                  }}
                >
                  🐈
                </button>
                <button 
                  className={`filter-btn ${filter === "other" ? "active" : ""}`}
                  onClick={() => {
                    setFilter("other");
                    setCurrentPage(0);
                  }}
                  style={{
                    padding: '0.4rem 0.8rem',
                    fontSize: '0.8rem'
                  }}
                >
                  🐾
                </button>
              </div>

              {/* Información del filtro actual */}
              <div className="filter-info" style={{ fontSize: '0.75rem' }}>
                {searchTerm && (
                  <span className="search-tag">
                    "{searchTerm}"
                    <button onClick={() => setSearchTerm('')}>×</button>
                  </span>
                )}
                {filter !== "all" && (
                  <span className="filter-tag">
                    {filter === "dogs" ? "Perros" : filter === "cats" ? "Gatos" : "Otras"}
                    <button onClick={() => setFilter('all')}>×</button>
                  </span>
                )}
              </div>
            </div>
          </section>

          {/* Sección de Todas las Mascotas */}
          <section className="pets-section all-pets-section" style={{ 
            padding: '1rem 1rem 2rem'
          }}>
            <div className="section-header" style={{ marginBottom: '1.5rem' }}>
              <h2 style={{ fontSize: '1.3rem', marginBottom: '0.3rem' }}>
                {filter === "all" ? "Todas las Mascotas" : 
                 filter === "dogs" ? "Perritos" :
                 filter === "cats" ? "Gatitos" : "Otras Mascotas"}
              </h2>
              <p style={{ fontSize: '0.85rem' }}>
                {filteredMascotas.length === 0 ? "No hay mascotas que coincidan" :
                 `${filteredMascotas.length} mascota${filteredMascotas.length !== 1 ? 's' : ''} encontrada${filteredMascotas.length !== 1 ? 's' : ''}`}
              </p>
            </div>

            {loading ? (
              <div className="loading-container" style={{ padding: '2rem' }}>
                <div className="loading-spinner"></div>
                <p style={{ fontSize: '0.9rem' }}>Cargando mascotas...</p>
              </div>
            ) : (
              <>
                <div className="mascotas-grid all-pets-grid">
                  {currentMascotas.length > 0 ? (
                    currentMascotas.map((mascota, index) => {
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
                            
                            {/* Botón Adóptame - Solo para ADOPTANTE */}
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
                            <h3 style={{ fontSize: '1.1rem' }}>{mascotaName}</h3>
                            <p className="pet-species" style={{ fontSize: '0.8rem' }}>
                              {mascota.species === 'PERRO' ? '🐕 Perro' : 
                               mascota.species === 'GATO' ? '🐈 Gato' : 
                               '🐾 ' + (mascota.otherspecies || mascota.especie || 'Otra especie')}
                              {mascota.race && ` • ${mascota.race}`}
                              {mascota.raza && ` • ${mascota.raza}`}
                            </p>
                            <p className="pet-description" style={{ fontSize: '0.8rem' }}>
                              {mascota.description || mascota.descripcion || "Buscando un hogar lleno de amor."}
                            </p>
                            <div className="pet-tags">
                              {mascota.childFriendly && <span className="tag" style={{ fontSize: '0.7rem' }}>👶 Niños</span>}
                              {mascota.sterilization && <span className="tag" style={{ fontSize: '0.7rem' }}>✂️ Ester.</span>}
                              {mascota.vaccinated && <span className="tag" style={{ fontSize: '0.7rem' }}>💉 Vac.</span>}
                              <span className="tag" style={{ fontSize: '0.7rem' }}>❤️ Hogar</span>
                            </div>
                            
                            {/* Mensaje informativo para usuarios no adoptantes */}
                            {isAuthenticated && !canAdoptPets && (
                              <div className="adoption-info">
                                <p className="info-text" style={{ fontSize: '0.7rem' }}>
                                  ⓘ Solo adoptantes pueden adoptar
                                </p>
                              </div>
                            )}
                            
                            {/* Mensaje para usuarios no autenticados */}
                            {!isAuthenticated && (
                              <div className="adoption-info">
                                <p className="info-text" style={{ fontSize: '0.7rem' }}>
                                  ⓘ <Link to="/login">Inicia sesión</Link> para adoptar
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
                      <h3 style={{ fontSize: '1.1rem' }}>No encontramos mascotas</h3>
                      <p style={{ fontSize: '0.85rem' }}>Intenta con otros filtros o modifica tu búsqueda.</p>
                      <button 
                        className="reset-filters-btn"
                        onClick={() => {
                          setFilter('all');
                          setSearchTerm('');
                        }}
                        style={{ fontSize: '0.8rem', padding: '0.5rem 1rem' }}
                      >
                        Mostrar todas
                      </button>
                    </div>
                  )}
                </div>

                {/* Paginación */}
                <Pagination />

                {/* Volver al inicio */}
                
              </>
            )}
          </section>
        </main>

        {/* Footer */}
        <footer className="main-footer" style={{ padding: '1.5rem 2rem 1rem' }}>
          <div className="footer-content">
            <div className="footer-section">
              <div className="logo">
                <span className="paw">🐾</span>
                <h3 style={{ fontSize: '1.1rem' }}>PetConnect</h3>
              </div>
              <p style={{ fontSize: '0.8rem' }}>Conectando mascotas con hogares</p>
            </div>
          </div>
          <div className="footer-bottom">
            <p style={{ fontSize: '0.75rem' }}>&copy; 2024 PetConnect. Todos los derechos reservados.</p>
          </div>
        </footer>
      </div>

      {/* Modal de Adopción */}
      <AdoptionModal
        mascota={selectedMascota}
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onSuccess={handleAdoptionSuccess}
      />
    </div>
  );
}