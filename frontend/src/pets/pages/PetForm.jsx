// PetForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import "../styles/petform.css";

const ESPECIE_MASCOTA = {
  PERRO: "PERRO",
  GATO: "GATO", 
  OTRO: "OTRO"
};

const SEXO_MASCOTA = {
  MACHO: "MACHO",
  HEMBRA: "HEMBRA"
};

// Configuración de axios directamente en el componente
const api = axios.create({
  baseURL: "http://localhost:9494/api/petconnect/mascotas",
  timeout: 10000,
});

// Interceptor para agregar el token automáticamente
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  config.headers['Content-Type'] = 'application/json';
  return config;
});

// Funciones API directamente en el componente
const getPet = (id) => api.get(`/${id}`);
const createPet = (data) => api.post("/save", data);
const updatePet = (id, data) => api.patch(`/update/${id}`, data);

export default function PetForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [currentTab, setCurrentTab] = useState(1);
  const [loading, setLoading] = useState(false);
  const [validationErrors, setValidationErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [form, setForm] = useState({
    name: "",
    species: "",
    otherspecies: "",
    race: "",
    birthDate: "",
    sex: "",
    childFriendly: false,
    requiresAmpleSpace: false,
    sterilization: false,
    vaccines: "",
    description: "",
    imageUrl: "",
  });

  useEffect(() => {
    if (id) loadPet();
    testConnection();
  }, [id]);

  // Función para probar la conexión básica
  const testConnection = async () => {
    try {
      console.log("🧪 Probando conexión con el backend...");
      const response = await fetch("http://localhost:9494/api/petconnect/mascotas/List");
      console.log("✅ Backend responde. Status:", response.status);
      return true;
    } catch (error) {
      console.error("❌ No se puede conectar al backend:", error);
      return false;
    }
  };

  // Función para formatear fecha de input (YYYY-MM-DD) a backend (dd/MM/yyyy)
  function formatDateForBackend(dateString) {
    if (!dateString) return null;
    
    try {
      const [year, month, day] = dateString.split('-');
      
      if (!year || !month || !day) {
        console.warn("Formato de fecha inválido:", dateString);
        return null;
      }
      
      return `${day.padStart(2, '0')}/${month.padStart(2, '0')}/${year}`;
    } catch (error) {
      console.error("Error formateando fecha para backend:", error);
      return null;
    }
  }

  // Función para formatear fecha del backend (dd/MM/yyyy) a input (YYYY-MM-DD)
  function formatDateForInput(dateString) {
    if (!dateString) return "";
    
    try {
      if (dateString.includes('/')) {
        const parts = dateString.split('/');
        if (parts.length === 3) {
          const day = parts[0];
          const month = parts[1];
          const year = parts[2];
          
          return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
        }
      }
      
      const date = new Date(dateString);
      if (isNaN(date.getTime())) {
        console.warn("Fecha inválida:", dateString);
        return "";
      }
      
      return date.toISOString().split('T')[0];
    } catch (error) {
      console.error("Error formateando fecha para input:", error);
      return "";
    }
  }

  async function loadPet() {
    try {
      setLoading(true);
      const res = await getPet(id);
      console.log("📥 Datos recibidos del backend:", res.data);
      
      const petData = {
        ...res.data,
        childFriendly: res.data.childFriendly || false,
        requiresAmpleSpace: res.data.requiresAmpleSpace || false,
        sterilization: res.data.sterilization || false,
        birthDate: res.data.birthDate ? formatDateForInput(res.data.birthDate) : ""
      };
      
      setForm(petData);
    } catch (error) {
      console.error("Error cargando mascota:", error);
      alert("Error al cargar la mascota");
    } finally {
      setLoading(false);
    }
  }

  function handleChange(e) {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value
    }));

    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: ""
      }));
    }
  }

  // Función solo para mostrar en UI
  const calculateAge = (birthDate) => {
    if (!birthDate) return null;
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
      age--;
    }
    return age.toString();
  };

  // Función solo para validar (sin formatear) - SOLO SE USA EN SUBMIT
  function validateForm(formData) {
    const errors = {};

    if (!formData.name?.trim()) {
      errors.name = "El nombre es requerido";
    }
    
    if (!formData.species) {
      errors.species = "La especie es requerida";
    }

    if (formData.species === ESPECIE_MASCOTA.OTRO && !formData.otherspecies?.trim()) {
      errors.otherspecies = "Debe especificar la especie";
    }

    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors);
      throw new Error("Por favor complete los campos obligatorios");
    }

    setValidationErrors({});
  }

  // Función solo para formatear (sin validar)
  function formatDataForBackend(formData) {
    const formattedData = {
      name: formData.name?.trim(),
      species: formData.species,
      otherspecies: formData.species === ESPECIE_MASCOTA.OTRO ? formData.otherspecies?.trim() : null,
      race: formData.race?.trim() || null,
      birthDate: formatDateForBackend(formData.birthDate),
      sex: formData.sex || null,
      childFriendly: Boolean(formData.childFriendly),
      requiresAmpleSpace: Boolean(formData.requiresAmpleSpace),
      sterilization: Boolean(formData.sterilization),
      vaccines: formData.vaccines?.trim() || null,
      description: formData.description?.trim() || null,
      imageUrl: formData.imageUrl?.trim() || null,
    };

    console.log("✅ Datos formateados para enviar:", formattedData);
    return formattedData;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setIsSubmitting(true);
    setLoading(true);
    setValidationErrors({});

    try {
      console.log("🔍 Datos del formulario ANTES de formatear:", form);
      
      // Primero validar (SOLO EN SUBMIT)
      validateForm(form);
      
      // Luego formatear
      const submitData = formatDataForBackend(form);
      
      console.log("📤 Enviando datos al servidor:", JSON.stringify(submitData, null, 2));
      console.log("📅 Fecha formateada para backend:", submitData.birthDate);

      const token = localStorage.getItem("token");
      console.log("🔑 Token disponible:", !!token);

      const isConnected = await testConnection();
      if (!isConnected) {
        throw new Error("No hay conexión con el backend");
      }

      if (id) {
        await updatePet(id, submitData);
        alert("Mascota actualizada exitosamente!");
      } else {
        await createPet(submitData);
        alert("Mascota creada exitosamente!");
      }
      
      navigate("/");
    } catch (error) {
      console.error("❌ Error completo:", error);
      console.error("📋 Response data:", error.response?.data);
      console.error("🔧 Response status:", error.response?.status);
      
      let errorMessage = "Error al guardar la mascota.\n\n";
      
      // Si es error de validación del frontend
      if (error.message === "Por favor complete los campos obligatorios") {
        errorMessage = error.message + "\n\nRevise los campos marcados en rojo.";
        setCurrentTab(1); // Ir a la primera pestaña para mostrar errores
      } 
      // Si es error del backend
      else if (error.response?.data) {
        const errorData = error.response.data;
        console.error("🐛 Error del backend:", errorData);
        
        if (typeof errorData === 'string') {
          errorMessage += `Detalles: ${errorData}`;
        } else if (errorData.message) {
          errorMessage += `Mensaje: ${errorData.message}`;
        } else if (errorData.error) {
          errorMessage += `Error: ${errorData.error}`;
        } else {
          errorMessage += `Respuesta: ${JSON.stringify(errorData)}`;
        }
      } else if (error.message) {
        errorMessage += `Error: ${error.message}`;
      } else {
        errorMessage += "Error desconocido. Verifica la consola para más detalles.";
      }
      
      alert(errorMessage);
    } finally {
      setLoading(false);
      setIsSubmitting(false);
    }
  }

  // Navegación simple sin validación - CORREGIDO
  const nextTab = () => {
    // SOLO CAMBIAR PESTAÑA, SIN VALIDACIÓN
    setCurrentTab(prev => {
      const next = prev + 1;
      return next > 2 ? 2 : next;
    });
  };

  const prevTab = () => {
    // SOLO CAMBIAR PESTAÑA, SIN VALIDACIÓN
    setCurrentTab(prev => {
      const prevTab = prev - 1;
      return prevTab < 1 ? 1 : prevTab;
    });
  };

  if (loading && id) {
    return (
      <div className="pet-form-container">
        <div className="loading">Cargando mascota...</div>
      </div>
    );
  }

  return (
    <div className="pet-form-container">
      <div className="pet-form-header">
        <h1>{id ? "Editar Mascota" : "Crear Nueva Mascota"}</h1>
        <p>Completa la información de la mascota para {id ? "actualizar" : "agregar"} al sistema</p>
        
        <div style={{ 
          background: '#f8f9fa', 
          padding: '10px', 
          borderRadius: '8px', 
          marginTop: '10px',
          fontSize: '0.8rem',
          color: '#666'
        }}>
          <strong>Debug Info:</strong> 
          <br />Token: {localStorage.getItem("token") ? "✅ Presente" : "❌ Ausente"}
          <br />Backend: http://localhost:9494
          <br />Formato fecha: dd/MM/yyyy
        </div>
      </div>

      {/* Pestañas */}
      <div className="pet-form-tabs">
        <div 
          className={`pet-form-tab ${currentTab === 1 ? 'active' : ''}`}
          onClick={() => setCurrentTab(1)}
        >
          Información Básica
        </div>
        <div 
          className={`pet-form-tab ${currentTab === 2 ? 'active' : ''}`}
          onClick={() => setCurrentTab(2)}
        >
          Características
        </div>
      </div>

      <form onSubmit={handleSubmit}>
        {/* Pestaña 1: Información Básica */}
        <div className={`pet-form-tab-content ${currentTab === 1 ? 'active' : ''}`}>
          <div className="pet-form-grid">
            <div className="pet-input-group">
              <label htmlFor="name">Nombre *</label>
              <input
                id="name"
                name="name"
                type="text"
                value={form.name}
                onChange={handleChange}
                placeholder="Ej: Max, Luna, etc."
                className={validationErrors.name ? 'error' : ''}
              />
              {validationErrors.name && (
                <span className="error-message">{validationErrors.name}</span>
              )}
            </div>

            <div className="pet-input-group">
              <label htmlFor="species">Especie *</label>
              <select
                id="species"
                name="species"
                value={form.species}
                onChange={handleChange}
                className={validationErrors.species ? 'error' : ''}
              >
                <option value="">Selecciona una especie</option>
                <option value={ESPECIE_MASCOTA.PERRO}>Perro</option>
                <option value={ESPECIE_MASCOTA.GATO}>Gato</option>
                <option value={ESPECIE_MASCOTA.OTRO}>Otro</option>
              </select>
              {validationErrors.species && (
                <span className="error-message">{validationErrors.species}</span>
              )}
            </div>

            {form.species === ESPECIE_MASCOTA.OTRO && (
              <div className="pet-input-group full-width">
                <label htmlFor="otherspecies">Especifica la especie *</label>
                <input
                  id="otherspecies"
                  name="otherspecies"
                  type="text"
                  value={form.otherspecies}
                  onChange={handleChange}
                  placeholder="Ej: Conejo, Hámster, etc."
                  className={validationErrors.otherspecies ? 'error' : ''}
                />
                {validationErrors.otherspecies && (
                  <span className="error-message">{validationErrors.otherspecies}</span>
                )}
              </div>
            )}

            <div className="pet-input-group">
              <label htmlFor="race">Raza</label>
              <input
                id="race"
                name="race"
                type="text"
                value={form.race}
                onChange={handleChange}
                placeholder="Ej: Labrador, Siames, etc."
              />
            </div>

            <div className="pet-input-group">
              <label htmlFor="birthDate">Fecha de Nacimiento</label>
              <input
                id="birthDate"
                name="birthDate"
                type="date"
                value={form.birthDate}
                onChange={handleChange}
                max={new Date().toISOString().split('T')[0]}
              />
              {form.birthDate && (
                <small className="age-calculator">
                  Edad aproximada: {calculateAge(form.birthDate) || 0} años
                  <br />
                  <span style={{fontSize: '0.7rem', color: '#667eea'}}>
                    (Se enviará como: {formatDateForBackend(form.birthDate)})
                  </span>
                </small>
              )}
            </div>

            <div className="pet-input-group full-width">
              <label>Sexo</label>
              <div className="pet-radio-group">
                <label className="pet-radio-label">
                  <input
                    type="radio"
                    name="sex"
                    value={SEXO_MASCOTA.MACHO}
                    checked={form.sex === SEXO_MASCOTA.MACHO}
                    onChange={handleChange}
                  />
                  <span>Macho</span>
                </label>
                <label className="pet-radio-label">
                  <input
                    type="radio"
                    name="sex"
                    value={SEXO_MASCOTA.HEMBRA}
                    checked={form.sex === SEXO_MASCOTA.HEMBRA}
                    onChange={handleChange}
                  />
                  <span>Hembra</span>
                </label>
              </div>
            </div>
          </div>
        </div>

        {/* Pestaña 2: Características */}
        <div className={`pet-form-tab-content ${currentTab === 2 ? 'active' : ''}`}>
          <div className="pet-form-grid-full">
            <div className="pet-characteristics-grid">
              <div className="pet-checkbox-group">
                <label className="pet-checkbox-label">
                  <input
                    type="checkbox"
                    name="childFriendly"
                    checked={form.childFriendly}
                    onChange={handleChange}
                  />
                  <span>Amigable con niños</span>
                </label>

                <label className="pet-checkbox-label">
                  <input
                    type="checkbox"
                    name="requiresAmpleSpace"
                    checked={form.requiresAmpleSpace}
                    onChange={handleChange}
                  />
                  <span>Requiere espacio amplio</span>
                </label>

                <label className="pet-checkbox-label">
                  <input
                    type="checkbox"
                    name="sterilization"
                    checked={form.sterilization}
                    onChange={handleChange}
                  />
                  <span>Esterilizado/Castrado</span>
                </label>
              </div>
            </div>

            <div className="pet-input-group">
              <label htmlFor="vaccines">Vacunas</label>
              <textarea
                id="vaccines"
                name="vaccines"
                value={form.vaccines}
                onChange={handleChange}
                placeholder="Lista de vacunas aplicadas (separadas por coma)..."
                rows="3"
              />
            </div>

            <div className="pet-input-group">
              <label htmlFor="description">Descripción</label>
              <textarea
                id="description"
                name="description"
                value={form.description}
                onChange={handleChange}
                placeholder="Describe la personalidad, cuidados especiales, historia, etc."
                rows="4"
              />
            </div>

            <div className="pet-input-group">
              <label htmlFor="imageUrl">URL de la Imagen</label>
              <input
                id="imageUrl"
                name="imageUrl"
                type="url"
                value={form.imageUrl}
                onChange={handleChange}
                placeholder="https://ejemplo.com/imagen.jpg"
              />
              {form.imageUrl && (
                <div className="pet-image-preview">
                  <img 
                    src={form.imageUrl} 
                    alt="Vista previa" 
                    onError={(e) => {
                      e.target.style.display = 'none';
                      console.warn("❌ Error cargando imagen:", form.imageUrl);
                    }} 
                  />
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Navegación entre pestañas - CORREGIDO */}
        <div className="pet-tab-navigation">
          <div className="pet-tab-navigation-left">
            {currentTab > 1 ? (
              <button type="button" className="pet-tab-btn" onClick={prevTab}>
                Anterior
              </button>
            ) : (
              <button type="button" className="pet-tab-btn" onClick={() => navigate("/")}>
                Cancelar
              </button>
            )}
          </div>

          <div className="pet-tab-navigation-center">
            {currentTab < 2 ? (
              <button type="button" className="pet-tab-btn primary" onClick={nextTab}>
                Siguiente
              </button>
            ) : null}
          </div>

          <div className="pet-tab-navigation-right">
            <button 
              type="submit" 
              className="pet-tab-btn primary"
              disabled={isSubmitting}
            >
              {isSubmitting ? "Guardando..." : (id ? "Actualizar" : "Crear")} Mascota
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}