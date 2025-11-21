// PetForm.jsx
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createPet, getPet, updatePet } from "../../api/pets";
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

export default function PetForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [currentTab, setCurrentTab] = useState(1);
  const [loading, setLoading] = useState(false);

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
  }, [id]);

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

  function formatDateForInput(dateString) {
    if (!dateString) return "";
    
    try {
      const date = new Date(dateString);
      if (isNaN(date.getTime())) {
        console.warn("Fecha inválida:", dateString);
        return "";
      }
      
      return date.toISOString().split('T')[0];
    } catch (error) {
      console.error("Error formateando fecha:", error);
      return "";
    }
  }

  function handleChange(e) {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value
    }));
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

  // Agrega esta función en PetForm.jsx para probar la conexión básica
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

// Llama a esta función en el useEffect o antes de enviar
useEffect(() => {
  testConnection(); // Para verificar la conexión al cargar el componente
}, []);

  // Función para validar y formatear los datos para el backend
  function validateAndFormatData(formData) {
    const formattedData = {
      name: formData.name?.trim(),
      species: formData.species,
      otherspecies: formData.species === ESPECIE_MASCOTA.OTRO ? formData.otherspecies?.trim() : null,
      race: formData.race?.trim() || null,
      birthDate: formData.birthDate || null,
      // ⚠️ NO enviar 'age' - se calcula en el backend
      sex: formData.sex || null,
      childFriendly: Boolean(formData.childFriendly),
      requiresAmpleSpace: Boolean(formData.requiresAmpleSpace),
      sterilization: Boolean(formData.sterilization),
      vaccines: formData.vaccines?.trim() || null,
      description: formData.description?.trim() || null,
      imageUrl: formData.imageUrl?.trim() || null,
    };

    // Validaciones básicas
    if (!formattedData.name) {
      throw new Error("El nombre es requerido");
    }
    
    if (!formattedData.species) {
      throw new Error("La especie es requerida");
    }

    if (formattedData.species === ESPECIE_MASCOTA.OTRO && !formattedData.otherspecies) {
      throw new Error("Debe especificar la especie");
    }

    console.log("✅ Datos formateados para enviar:", formattedData);
    return formattedData;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);

    try {
      console.log("🔍 Datos del formulario ANTES de formatear:", form);
      
      // Validar y formatear datos
      const submitData = validateAndFormatData(form);
      
      console.log("📤 Enviando datos al servidor:", JSON.stringify(submitData, null, 2));

      const token = localStorage.getItem("token");
      console.log("🔑 Token disponible:", !!token);

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
      
      if (error.response?.data) {
        const errorData = error.response.data;
        console.error("🐛 Error del backend:", errorData);
        
        let errorMessage = "Error del servidor: ";
        
        if (errorData.message) {
          errorMessage += errorData.message;
        } else if (errorData.error) {
          errorMessage += errorData.error;
        } else {
          errorMessage += JSON.stringify(errorData);
        }
        
        alert(errorMessage);
      } else if (error.message && !error.response) {
        alert(`Error de validación: ${error.message}`);
      } else {
        alert("Error al guardar la mascota. Verifica la conexión con el servidor.");
      }
    } finally {
      setLoading(false);
    }
  }

  const nextTab = () => setCurrentTab(prev => Math.min(prev + 1, 3));
  const prevTab = () => setCurrentTab(prev => Math.max(prev - 1, 1));

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
        <div 
          className={`pet-form-tab ${currentTab === 3 ? 'active' : ''}`}
          onClick={() => setCurrentTab(3)}
        >
          Información Adicional
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
                required
                placeholder="Ej: Max, Luna, etc."
              />
            </div>

            <div className="pet-input-group">
              <label htmlFor="species">Especie *</label>
              <select
                id="species"
                name="species"
                value={form.species}
                onChange={handleChange}
                required
              >
                <option value="">Selecciona una especie</option>
                <option value={ESPECIE_MASCOTA.PERRO}>Perro</option>
                <option value={ESPECIE_MASCOTA.GATO}>Gato</option>
                <option value={ESPECIE_MASCOTA.OTRO}>Otro</option>
              </select>
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
                  required={form.species === ESPECIE_MASCOTA.OTRO}
                  placeholder="Ej: Conejo, Hámster, etc."
                />
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
          </div>
        </div>

        {/* Pestaña 3: Información Adicional */}
        <div className={`pet-form-tab-content ${currentTab === 3 ? 'active' : ''}`}>
          <div className="pet-form-grid-full">
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

        {/* Navegación entre pestañas */}
        <div className="pet-tab-navigation">
          {currentTab > 1 ? (
            <button type="button" className="pet-tab-btn" onClick={prevTab}>
              Anterior
            </button>
          ) : (
            <button type="button" className="pet-tab-btn" onClick={() => navigate("/")}>
              Cancelar
            </button>
          )}
          
          {currentTab < 3 ? (
            <button type="button" className="pet-tab-btn primary" onClick={nextTab}>
              Siguiente
            </button>
          ) : (
            <button 
              type="submit" 
              className="pet-tab-btn primary"
              disabled={loading}
            >
              {loading ? "Guardando..." : (id ? "Actualizar" : "Crear")} Mascota
            </button>
          )}
        </div>
      </form>
    </div>
  );
}