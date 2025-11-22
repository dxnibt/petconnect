// src/pets/pages/PetUpdateForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import "../styles/petform.css";

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
const updatePet = (id, data) => api.put(`/update/${id}`, data);

export default function PetUpdateForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  console.log("🎯 PetUpdateForm montado");
  console.log("📌 Parámetro ID:", id);
  console.log("🔍 Token disponible:", !!localStorage.getItem("token"));

  const [form, setForm] = useState({
    name: "",
    species: "",
    otherspecies: "",
    race: "",
    birthDate: "",
    sex: "",
    age: "",
    childFriendly: false,
    requiresAmpleSpace: false,
    sterilization: false,
    vaccines: "",
    description: "",
    imageUrl: "",
  });

  useEffect(() => {
    console.log("🔍 ID de mascota recibido:", id);
    if (id) {
      loadPet();
    } else {
      console.error("❌ No se recibió ID de mascota");
      alert("Error: No se especificó qué mascota editar");
      navigate("/");
    }
  }, [id]);

  async function loadPet() {
    try {
      setLoading(true);
      console.log("🔄 Cargando mascota con ID:", id);
      
      const res = await getPet(id);
      console.log("📥 Respuesta completa:", res);
      console.log("📥 Datos de mascota:", res.data);
      
      if (!res.data) {
        throw new Error("No se recibieron datos de la mascota");
      }
      
      const petData = {
        ...res.data,
        childFriendly: res.data.childFriendly || false,
        requiresAmpleSpace: res.data.requiresAmpleSpace || false,
        sterilization: res.data.sterilization || false,
      };
      
      setForm(petData);
      console.log("✅ Formulario actualizado con datos:", petData);
      
    } catch (error) {
      console.error("❌ Error cargando mascota:", error);
      console.error("📋 Detalles error:", error.response?.data);
      alert(`Error al cargar la mascota: ${error.message}`);
      navigate("/");
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
  }

  // Función solo para formatear (sin validar)
  function formatDataForBackend(formData) {
    const formattedData = {
      // Campos que SÍ pueden modificar los refugios
      childFriendly: Boolean(formData.childFriendly),
      requiresAmpleSpace: Boolean(formData.requiresAmpleSpace),
      sterilization: Boolean(formData.sterilization),
      vaccines: formData.vaccines?.trim() || "",
      description: formData.description?.trim() || "",
      imageUrl: formData.imageUrl?.trim() || "",
    };

    console.log("✅ Datos formateados para enviar:", formattedData);
    return formattedData;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setIsSubmitting(true);
    setLoading(true);

    try {
      console.log("🔍 DEBUG - Verificando autenticación:");
      const token = localStorage.getItem("token");
      console.log("🔑 Token disponible:", !!token);
      
      if (token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]));
          console.log("👤 Usuario en token:", payload.sub);
          console.log("🎭 Roles en token:", payload.roles || payload.authorities || "NO HAY ROLES");
        } catch (e) {
          console.log("❌ Error decodificando token:", e);
        }
      }

      console.log("🔍 Datos del formulario ANTES de formatear:", form);
      
      const submitData = formatDataForBackend(form);
      
      console.log("📤 Enviando datos al servidor:", JSON.stringify(submitData, null, 2));

      await updatePet(id, submitData);
      alert("Información de la mascota actualizada exitosamente!");
      
      navigate("/");
    } catch (error) {
      console.error("❌ Error completo:", error);
      console.error("📋 Response data:", error.response?.data);
      console.error("🔧 Response status:", error.response?.status);
      console.error("🔧 Response headers:", error.response?.headers);
      
      let errorMessage = "Error al actualizar la mascota.\n\n";
      
      if (error.response?.data) {
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

  // Función alternativa usando FormData (descomenta si la anterior no funciona)
  async function handleSubmitAlternative(e) {
    e.preventDefault();
    setIsSubmitting(true);
    setLoading(true);

    try {
      const token = localStorage.getItem("token");
      
      // Crear FormData en lugar de JSON
      const formData = new FormData();
      formData.append('childFriendly', form.childFriendly);
      formData.append('requiresAmpleSpace', form.requiresAmpleSpace);
      formData.append('sterilization', form.sterilization);
      formData.append('vaccines', form.vaccines || '');
      formData.append('description', form.description || '');
      formData.append('imageUrl', form.imageUrl || '');

      console.log("📤 Enviando FormData:");
      for (let [key, value] of formData.entries()) {
        console.log(`  ${key}:`, value);
      }

      // Usar axios directamente sin el interceptor
      await axios.patch(`http://localhost:9494/api/petconnect/mascotas/update/${id}`, formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });

      alert("Información de la mascota actualizada exitosamente!");
      navigate("/");
    } catch (error) {
      console.error("❌ Error completo:", error);
      console.error("📋 Response data:", error.response?.data);
      
      let errorMessage = "Error al actualizar la mascota.\n\n";
      
      if (error.response?.data) {
        const errorData = error.response.data;
        if (typeof errorData === 'string') {
          errorMessage += `Detalles: ${errorData}`;
        } else if (errorData.message) {
          errorMessage += `Mensaje: ${errorData.message}`;
        } else {
          errorMessage += `Respuesta: ${JSON.stringify(errorData)}`;
        }
      } else {
        errorMessage += `Error: ${error.message}`;
      }
      
      alert(errorMessage);
    } finally {
      setLoading(false);
      setIsSubmitting(false);
    }
  }

  if (loading) {
    return (
      <div className="pet-form-container">
        <div className="loading">Cargando mascota...</div>
      </div>
    );
  }

  return (
    <div className="pet-form-container">
      <div className="pet-form-header">
        <h1>Actualizar Información de Mascota</h1>
        <p>Como refugio, puedes actualizar la información médica y características de la mascota</p>
        
        <div style={{ 
          background: '#e3f2fd', 
          padding: '10px', 
          borderRadius: '8px', 
          marginTop: '10px',
          fontSize: '0.8rem',
          color: '#1565c0',
          border: '1px solid #90caf9'
        }}>
          <strong>⚠️ Información importante:</strong> 
          <br />• Solo puedes modificar información médica y características
          <br />• Los datos básicos (nombre, especie, raza, etc.) están bloqueados
        </div>
      </div>

      <form onSubmit={handleSubmit}>
        {/* Sección de Información Básica (SOLO LECTURA) */}
        <div className="form-section">
          <h3 className="section-title">Información Básica (Solo lectura)</h3>
          <div className="pet-form-grid">
            <div className="pet-input-group">
              <label>Nombre</label>
              <input
                type="text"
                value={form.name || "No especificado"}
                readOnly
                className="readonly-field"
              />
            </div>

            <div className="pet-input-group">
              <label>Especie</label>
              <input
                type="text"
                value={form.species === 'PERRO' ? 'Perro' : 
                       form.species === 'GATO' ? 'Gato' : 
                       form.otherspecies || 'Otra especie'}
                readOnly
                className="readonly-field"
              />
            </div>

            <div className="pet-input-group">
              <label>Raza</label>
              <input
                type="text"
                value={form.race || "No especificada"}
                readOnly
                className="readonly-field"
              />
            </div>

            <div className="pet-input-group">
              <label>Sexo</label>
              <input
                type="text"
                value={form.sex === 'MACHO' ? 'Macho' : 
                       form.sex === 'HEMBRA' ? 'Hembra' : 
                       'No especificado'}
                readOnly
                className="readonly-field"
              />
            </div>

            <div className="pet-input-group">
              <label>Fecha de Nacimiento</label>
              <input
                type="text"
                value={form.birthDate || "No especificada"}
                readOnly
                className="readonly-field"
              />
            </div>

            <div className="pet-input-group">
              <label>Edad</label>
              <input
                type="text"
                value={form.age ? `${form.age} años` : "No especificada"}
                readOnly
                className="readonly-field"
              />
            </div>
          </div>
        </div>

        {/* Sección de Características (EDITABLE) */}
        <div className="form-section">
          <h3 className="section-title">Características y Salud (Editable)</h3>
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
              <small className="field-help">
                Ej: Rabia, Moquillo, Parvovirus, etc.
              </small>
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
              <small className="field-help">
                Esta información ayuda a los adoptantes a conocer mejor a la mascota
              </small>
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
              <small className="field-help">
                Actualiza la foto de la mascota para mostrar su estado actual
              </small>
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

        {/* Botones de acción */}
        <div className="pet-form-actions">
          <button 
            type="button" 
            className="pet-tab-btn" 
            onClick={() => navigate("/")}
          >
            Cancelar
          </button>
          <button 
            type="submit" 
            className="pet-tab-btn primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Guardando..." : "Actualizar Información"}
          </button>
          
          {/* Botón alternativo (descomenta si necesitas probar FormData) */}
          {/*
          <button 
            type="button" 
            className="pet-tab-btn secondary"
            onClick={handleSubmitAlternative}
            disabled={isSubmitting}
            style={{marginLeft: '10px', background: '#ff9800'}}
          >
            {isSubmitting ? "Guardando..." : "Probar FormData"}
          </button>
          */}
        </div>
      </form>
    </div>
  );
}