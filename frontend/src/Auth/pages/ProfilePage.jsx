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
        `http://localhost:8181/api/petconnect/usuario/${userId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      console.log("📥 Datos COMPLETOS del usuario:", res.data);
      console.log("📅 registrationDate:", res.data.registrationDate);
      console.log("🎂 birthDate:", res.data.birthDate);
      console.log("🔍 Todos los campos disponibles:", Object.keys(res.data));

      setEditForm(res.data);
      updateUserData(res.data);
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
          `http://localhost:8181/api/petconnect/refugio/${userId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        setMascotas(mascotasRes.data || []);
      } else if (userRole === "ADOPTANTE") {
        try {
          const solicitudesRes = await axios.get(
            `http://localhost:8181/api/petconnect/adoptante/${userId}`,
            { headers: { Authorization: `Bearer ${token}` } }
          );
          setSolicitudes(solicitudesRes.data || []);
        } catch (solicitudesError) {
          console.warn("⚠️ No se pudieron cargar las solicitudes:", solicitudesError.message);
          setSolicitudes([]);
        }
      }
    } catch (error) {
      console.error("Error cargando datos adicionales:", error);
    } finally {
      setLoading(false);
    }
  };

  // 🔹 Editar perfil - CON PUT
  const handleEdit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      
      if (!token) {
        alert("No hay token de autenticación. Por favor, inicia sesión nuevamente.");
        return;
      }

      let endpoint = "";
      let dataToSend = {};

      if (userRole === "ADOPTANTE") {
        endpoint = `http://localhost:8181/api/petconnect/adoptantes/update/${userId}`;
        dataToSend = {
          name: editForm.name || "",
          phoneNumber: editForm.phoneNumber || "",
          city: editForm.city || "",
          address: editForm.address || "",
          // Documento NO se incluye - no se puede modificar
          gender: editForm.gender || "",
          otherGender: editForm.otherGender || "",
          // Fecha de nacimiento NO se incluye - no se puede modificar
          monthlySalary: editForm.monthlySalary || 0,
          housingType: editForm.housingType || "",
          hasYard: editForm.hasYard || false,
          petExperience: editForm.petExperience || false,
          hasOtherPets: editForm.hasOtherPets || false,
          hasChildren: editForm.hasChildren || false,
          hoursAwayFromHome: editForm.hoursAwayFromHome || 0,
          preferredAnimalType: editForm.preferredAnimalType || "",
          otherPreferredAnimalType: editForm.otherPreferredAnimalType || "",
          preferredPetSize: editForm.preferredPetSize || "",
          activityLevel: editForm.activityLevel || "",
          personalDescription: editForm.personalDescription || ""
        };
      } else if (userRole === "REFUGIO") {
        endpoint = `http://localhost:8181/api/petconnect/refugios/update/${userId}`;
        dataToSend = {
          name: editForm.name || "",
          phoneNumber: editForm.phoneNumber || "",
          city: editForm.city || "",
          address: editForm.address || "",
          description: editForm.description || ""
          // NIT NO se incluye - no se puede modificar
        };
      }

      console.log("📤 Enviando PUT a:", endpoint);
      console.log("📦 Datos enviados:", dataToSend);

      // USAMOS PUT EN LUGAR DE PATCH
      const response = await axios.put(endpoint, dataToSend, {
        headers: { 
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      console.log("✅ Respuesta del servidor:", response.data);

      // Actualizar el contexto global con los nuevos datos
      updateUserData(editForm);
      
      setEditing(false);
      alert("Perfil actualizado exitosamente!");
      
      // Recargar los datos para asegurar consistencia
      loadUserProfile();
      
    } catch (error) {
      console.error("❌ Error actualizando perfil:", error);
      
      if (error.code === 'ERR_NETWORK') {
        alert("Error de conexión. Verifica que el servidor esté ejecutándose.");
      } else if (error.response?.status === 403 || error.response?.status === 401) {
        alert("Sesión expirada. Por favor, inicia sesión nuevamente.");
      } else if (error.response?.status === 404) {
        alert("Endpoint no encontrado. Verifica la URL del servicio.");
      } else {
        alert("Error al actualizar el perfil: " + (error.response?.data?.message || error.message));
      }
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setEditForm(prev => ({ 
      ...prev, 
      [name]: type === 'checkbox' ? checked : value 
    }));
  };

  // 🔹 Imagen según rol o usar la del perfil
  const getProfilePhoto = () => {
    if (userData?.profilePicture) {
      return userData.profilePicture;
    }
    
    const defaultPhotos = {
      ADOPTANTE: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&h=400&fit=crop&crop=face",
      REFUGIO: "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=400&h=400&fit=crop&crop=center",
      ADMIN: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop&face"
    };
    return defaultPhotos[userRole] || defaultPhotos.ADOPTANTE;
  };

  const getFullName = () => {
    return userData?.name || userEmail?.split("@")[0] || "Usuario";
  };

  // 🔹 Formatear fecha - VERSIÓN CORREGIDA (sin problemas de timezone)
  const formatDate = (dateString) => {
    if (!dateString) {
      return "No especificado";
    }
    
    try {
      // Para fechas en formato YYYY-MM-DD (como birthDate), usar split para evitar problemas de timezone
      if (typeof dateString === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(dateString)) {
        const [year, month, day] = dateString.split('-');
        const date = new Date(year, month - 1, day); // month - 1 porque los meses en JS van de 0-11
        return date.toLocaleDateString('es-ES', {
          year: 'numeric',
          month: 'long',
          day: 'numeric'
        });
      }
      
      // Para otros formatos de fecha
      const date = new Date(dateString);
      if (!isNaN(date.getTime())) {
        return date.toLocaleDateString('es-ES', {
          year: 'numeric',
          month: 'long',
          day: 'numeric'
        });
      }
      
      return "No especificado";
    } catch (error) {
      console.error("❌ Error formateando fecha:", error, "Valor:", dateString);
      return "No especificado";
    }
  };

  // 🔹 Formatear fecha para input type="date" (formato YYYY-MM-DD)
  const formatDateForInput = (dateString) => {
    if (!dateString) return "";
    
    try {
      // Si ya está en formato YYYY-MM-DD, devolverlo tal cual
      if (typeof dateString === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(dateString)) {
        return dateString;
      }
      
      const date = new Date(dateString);
      if (!isNaN(date.getTime())) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
      
      return "";
    } catch (error) {
      console.error("Error formateando fecha para input:", error);
      return "";
    }
  };

  // 🔹 Obtener fecha de registro (busca en diferentes campos)
  const getRegistrationDate = () => {
    const possibleDateFields = [
      userData?.registrationDate,
      userData?.createdAt,
      userData?.createDate,
      userData?.fechaRegistro,
      userData?.fechaCreacion
    ];
    
    for (const dateField of possibleDateFields) {
      if (dateField) {
        return formatDate(dateField);
      }
    }
    
    return "Fecha no disponible";
  };

  // 🔹 Formatear valores booleanos
  const formatBoolean = (value) => {
    return value ? "Sí" : "No";
  };

  // 🔹 Formatear salario
  const formatSalary = (salary) => {
    if (!salary) return "No especificado";
    return new Intl.NumberFormat('es-ES', {
      style: 'currency',
      currency: 'USD'
    }).format(salary);
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
              
              {/* Fecha de registro */}
              <div className="pc-registration-date">
                Miembro desde: {getRegistrationDate()}
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
                    {/* Información Básica - Para todos los roles */}
                    <div className="pc-field-group">
                      <h4>Información Básica</h4>
                      <div className="pc-info-grid">
                        <div className="pc-info-item"><label>Nombre completo:</label><span>{getFullName()}</span></div>
                        <div className="pc-info-item"><label>Email:</label><span>{userEmail}</span></div>
                        <div className="pc-info-item"><label>Teléfono:</label><span>{userData?.phoneNumber || "No especificado"}</span></div>
                        <div className="pc-info-item"><label>Ciudad:</label><span>{userData?.city || "No especificada"}</span></div>
                        <div className="pc-info-item"><label>Dirección:</label><span>{userData?.address || "No especificada"}</span></div>
                        
                        {/* Campos no editables según el rol */}
                        {userRole === "ADOPTANTE" && (
                          <>
                            <div className="pc-info-item"><label>Documento:</label><span>{userData?.document || "No especificado"}</span></div>
                            <div className="pc-info-item"><label>Fecha de nacimiento:</label><span>{formatDate(userData?.birthDate)}</span></div>
                          </>
                        )}
                        
                        {userRole === "REFUGIO" && (
                          <div className="pc-info-item"><label>NIT:</label><span>{userData?.document || userData?.nit || "No especificado"}</span></div>
                        )}
                      </div>
                    </div>

                    {/* Información Específica por Rol */}
                    {userRole === "ADOPTANTE" && (
                      <>
                        {/* Información Personal Adoptante */}
                        <div className="pc-field-group">
                          <h4>Información Personal</h4>
                          <div className="pc-info-grid">
                            <div className="pc-info-item"><label>Género:</label><span>{userData?.gender || "No especificado"}</span></div>
                          </div>
                        </div>

                        {/* Situación Económica y Vivienda */}
                        <div className="pc-field-group">
                          <h4>Situación Económica y Vivienda</h4>
                          <div className="pc-info-grid">
                            <div className="pc-info-item"><label>Salario mensual:</label><span>{formatSalary(userData?.monthlySalary)}</span></div>
                            <div className="pc-info-item"><label>Tipo de vivienda:</label><span>{userData?.housingType || "No especificado"}</span></div>
                            <div className="pc-info-item"><label>Horas fuera de casa:</label><span>{userData?.hoursAwayFromHome ? `${userData.hoursAwayFromHome} horas` : "No especificado"}</span></div>
                          </div>
                        </div>

                        {/* Preferencias de Adopción */}
                        <div className="pc-field-group">
                          <h4>Preferencias de Adopción</h4>
                          <div className="pc-info-grid">
                            <div className="pc-info-item"><label>Tipo de animal preferido:</label><span>{userData?.preferredAnimalType || "No especificado"}</span></div>
                            <div className="pc-info-item"><label>Tamaño preferido:</label><span>{userData?.preferredPetSize || "No especificado"}</span></div>
                            <div className="pc-info-item"><label>Nivel de actividad:</label><span>{userData?.activityLevel || "No especificado"}</span></div>
                          </div>
                        </div>

                        {/* Situación Familiar y Experiencia */}
                        <div className="pc-field-group">
                          <h4>Situación Familiar y Experiencia</h4>
                          <div className="pc-boolean-grid">
                            <div className="pc-info-item"><label>¿Tiene patio/jardín?:</label><span>{formatBoolean(userData?.hasYard)}</span></div>
                            <div className="pc-info-item"><label>¿Experiencia con mascotas?:</label><span>{formatBoolean(userData?.petExperience)}</span></div>
                            <div className="pc-info-item"><label>¿Tiene otras mascotas?:</label><span>{formatBoolean(userData?.hasOtherPets)}</span></div>
                            <div className="pc-info-item"><label>¿Tiene niños?:</label><span>{formatBoolean(userData?.hasChildren)}</span></div>
                          </div>
                        </div>

                        {/* Descripción Personal */}
                        <div className="pc-field-group">
                          <h4>Descripción Personal</h4>
                          <div className="pc-info-item pc-full-width">
                            <span>{userData?.personalDescription || "No hay descripción disponible"}</span>
                          </div>
                        </div>
                      </>
                    )}
                    
                    {userRole === "REFUGIO" && (
                      <div className="pc-field-group">
                        <h4>Información del Refugio</h4>
                        <div className="pc-info-item pc-full-width">
                          <label>Descripción del refugio:</label>
                          <span>{userData?.description || "No hay descripción disponible"}</span>
                        </div>
                      </div>
                    )}

                    <button className="pc-edit-btn" onClick={() => setEditing(true)}>
                      ✏️ Editar Perfil
                    </button>
                  </div>
                ) : (
                  <form className="pc-edit-form" onSubmit={handleEdit}>
                    <h3>Editar Información Personal</h3>
                    
                    <div className="pc-field-group">
                      <h4>Información Básica</h4>
                      <div className="pc-form-grid">
                        <div className="pc-form-group">
                          <label>Nombre *</label>
                          <input 
                            type="text" 
                            name="name" 
                            value={editForm.name || ""} 
                            onChange={handleInputChange} 
                            required 
                          />
                        </div>
                        <div className="pc-form-group">
                          <label>Teléfono</label>
                          <input 
                            type="tel" 
                            name="phoneNumber" 
                            value={editForm.phoneNumber || ""} 
                            onChange={handleInputChange} 
                          />
                        </div>
                        <div className="pc-form-group">
                          <label>Ciudad</label>
                          <input 
                            type="text" 
                            name="city" 
                            value={editForm.city || ""} 
                            onChange={handleInputChange} 
                          />
                        </div>
                        <div className="pc-form-group pc-full-width">
                          <label>Dirección</label>
                          <input 
                            type="text" 
                            name="address" 
                            value={editForm.address || ""} 
                            onChange={handleInputChange} 
                          />
                        </div>
                        
                        {/* Campos no editables - solo lectura */}
                        {userRole === "ADOPTANTE" && (
                          <>
                            <div className="pc-form-group">
                              <label>Documento</label>
                              <input 
                                type="text" 
                                name="document" 
                                value={editForm.document || ""} 
                                readOnly
                                disabled
                                className="pc-readonly-field"
                              />
                              <small className="pc-field-note">⚠️ Este campo no se puede modificar</small>
                            </div>
                            <div className="pc-form-group">
                              <label>Fecha de nacimiento</label>
                              <input 
                                type="date" 
                                name="birthDate" 
                                value={formatDateForInput(editForm.birthDate)} 
                                readOnly
                                disabled
                                className="pc-readonly-field"
                              />
                              <small className="pc-field-note">⚠️ Este campo no se puede modificar</small>
                            </div>
                          </>
                        )}
                        
                        {userRole === "REFUGIO" && (
                          <div className="pc-form-group">
                            <label>NIT</label>
                            <input 
                              type="text" 
                              name="document" 
                              value={editForm.document || editForm.nit || ""} 
                              readOnly
                              disabled
                              className="pc-readonly-field"
                            />
                            <small className="pc-field-note">⚠️ Este campo no se puede modificar</small>
                          </div>
                        )}
                      </div>
                    </div>

                    {userRole === "ADOPTANTE" && (
                      <>
                        <div className="pc-field-group">
                          <h4>Información Personal</h4>
                          <div className="pc-form-grid">
                            <div className="pc-form-group">
                              <label>Género</label>
                              <select 
                                name="gender" 
                                value={editForm.gender || ""} 
                                onChange={handleInputChange}
                              >
                                <option value="">Seleccionar género</option>
                                <option value="MASCULINO">Masculino</option>
                                <option value="FEMENINO">Femenino</option>
                                <option value="OTRO">Otro</option>
                              </select>
                            </div>
                          </div>
                        </div>

                        <div className="pc-field-group">
                          <h4>Situación Económica y Vivienda</h4>
                          <div className="pc-form-grid">
                            <div className="pc-form-group">
                              <label>Salario mensual</label>
                              <input 
                                type="number" 
                                name="monthlySalary" 
                                value={editForm.monthlySalary || ""} 
                                onChange={handleInputChange} 
                              />
                            </div>
                            <div className="pc-form-group">
                              <label>Tipo de vivienda</label>
                              <select 
                                name="housingType" 
                                value={editForm.housingType || ""} 
                                onChange={handleInputChange}
                              >
                                <option value="">Seleccionar tipo</option>
                                <option value="CASA">Casa</option>
                                <option value="APARTMENTO">Apartamento</option>
                                <option value="OTRO">Otro</option>
                              </select>
                            </div>
                            <div className="pc-form-group">
                              <label>Horas fuera de casa</label>
                              <input 
                                type="number" 
                                name="hoursAwayFromHome" 
                                value={editForm.hoursAwayFromHome || ""} 
                                onChange={handleInputChange} 
                              />
                            </div>
                          </div>
                        </div>

                        <div className="pc-field-group">
                          <h4>Preferencias de Adopción</h4>
                          <div className="pc-form-grid">
                            <div className="pc-form-group">
                              <label>Tipo de animal preferido</label>
                              <select 
                                name="preferredAnimalType" 
                                value={editForm.preferredAnimalType || ""} 
                                onChange={handleInputChange}
                              >
                                <option value="">Seleccionar tipo</option>
                                <option value="PERRO">Perro</option>
                                <option value="GATO">Gato</option>
                                <option value="OTRO">Otro</option>
                              </select>
                            </div>
                            <div className="pc-form-group">
                              <label>Tamaño preferido</label>
                              <select 
                                name="preferredPetSize" 
                                value={editForm.preferredPetSize || ""} 
                                onChange={handleInputChange}
                              >
                                <option value="">Seleccionar tamaño</option>
                                <option value="PEQUEÑA">Pequeño</option>
                                <option value="MEDIANA">Mediano</option>
                                <option value="GRANDE">Grande</option>
                              </select>
                            </div>
                            <div className="pc-form-group">
                              <label>Nivel de actividad</label>
                              <select 
                                name="activityLevel" 
                                value={editForm.activityLevel || ""} 
                                onChange={handleInputChange}
                              >
                                <option value="">Seleccionar nivel</option>
                                <option value="BAJO">Bajo</option>
                                <option value="MEDIO">Medio</option>
                                <option value="ALTO">Alto</option>
                              </select>
                            </div>
                          </div>
                        </div>

                        <div className="pc-field-group">
                          <h4>Situación Familiar y Experiencia</h4>
                          <div className="pc-form-grid">
                            <div className="pc-form-group">
                              <label>
                                <input 
                                  type="checkbox" 
                                  name="hasYard" 
                                  checked={editForm.hasYard || false} 
                                  onChange={handleInputChange} 
                                />
                                ¿Tiene patio/jardín?
                              </label>
                            </div>
                            <div className="pc-form-group">
                              <label>
                                <input 
                                  type="checkbox" 
                                  name="petExperience" 
                                  checked={editForm.petExperience || false} 
                                  onChange={handleInputChange} 
                                />
                                ¿Experiencia con mascotas?
                              </label>
                            </div>
                            <div className="pc-form-group">
                              <label>
                                <input 
                                  type="checkbox" 
                                  name="hasOtherPets" 
                                  checked={editForm.hasOtherPets || false} 
                                  onChange={handleInputChange} 
                                />
                                ¿Tiene otras mascotas?
                              </label>
                            </div>
                            <div className="pc-form-group">
                              <label>
                                <input 
                                  type="checkbox" 
                                  name="hasChildren" 
                                  checked={editForm.hasChildren || false} 
                                  onChange={handleInputChange} 
                                />
                                ¿Tiene niños?
                              </label>
                            </div>
                          </div>
                        </div>

                        <div className="pc-field-group">
                          <h4>Descripción Personal</h4>
                          <div className="pc-form-group pc-full-width">
                            <textarea 
                              name="personalDescription" 
                              value={editForm.personalDescription || ""} 
                              onChange={handleInputChange} 
                              rows="4" 
                              placeholder="Cuéntanos sobre ti y tu experiencia con mascotas..."
                            />
                          </div>
                        </div>
                      </>
                    )}

                    {userRole === "REFUGIO" && (
                      <div className="pc-field-group">
                        <h4>Información del Refugio</h4>
                        <div className="pc-form-group pc-full-width">
                          <label>Descripción del refugio</label>
                          <textarea 
                            name="description" 
                            value={editForm.description || ""} 
                            onChange={handleInputChange} 
                            rows="4" 
                            placeholder="Describe tu refugio, misión y servicios..."
                          />
                        </div>
                      </div>
                    )}

                    <div className="pc-form-actions">
                      <button 
                        type="button" 
                        className="pc-cancel-btn" 
                        onClick={() => setEditing(false)}
                      >
                        Cancelar
                      </button>
                      <button 
                        type="submit" 
                        className="pc-save-btn" 
                        disabled={loading}
                      >
                        {loading ? "Guardando..." : "💾 Guardar Cambios"}
                      </button>
                    </div>
                  </form>
                )}
              </div>
            )}

            {/* Otras pestañas */}
            {activeTab === "mascotas" && userRole === "REFUGIO" && (
              <div className="pc-tab-content">
                <h3>Mis Mascotas</h3>
                {mascotas.length === 0 ? (
                  <p>No tienes mascotas registradas.</p>
                ) : (
                  <div className="pc-mascotas-grid">
                    {mascotas.map(mascota => (
                      <div key={mascota.id} className="pc-mascota-card">
                        <h4>{mascota.nombre}</h4>
                        <p>Especie: {mascota.especie}</p>
                        <p>Edad: {mascota.edad}</p>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}

            {activeTab === "solicitudes" && userRole === "ADOPTANTE" && (
              <div className="pc-tab-content">
                <h3>Mis Solicitudes</h3>
                {solicitudes.length === 0 ? (
                  <p>No tienes solicitudes de adopción.</p>
                ) : (
                  <div className="pc-solicitudes-list">
                    {solicitudes.map(solicitud => (
                      <div key={solicitud.id} className="pc-solicitud-card">
                        <h4>Solicitud #{solicitud.id}</h4>
                        <p>Estado: {solicitud.estado}</p>
                        <p>Fecha: {formatDate(solicitud.fechaSolicitud)}</p>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}