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
    imageUrl: ""
  });

  useEffect(() => {
    if (id) loadPet();
  }, [id]);

  async function loadPet() {
    try {
      setLoading(true);
      const res = await getPet(id);
      const petData = {
        ...res.data,
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
    const date = new Date(dateString);
    return date.toISOString().split('T')[0];
  }

  function handleChange(e) {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);

    try {
      const submitData = {
        ...form,
        otherspecies: form.species === ESPECIE_MASCOTA.OTRO ? form.otherspecies : ""
      };

      if (id) {
        await updatePet(id, submitData);
        alert("Mascota actualizada exitosamente!");
      } else {
        await createPet(submitData);
        alert("Mascota creada exitosamente!");
      }
      
      navigate("/");
    } catch (error) {
      console.error("Error guardando mascota:", error);
      alert("Error al guardar la mascota. Por favor intenta nuevamente.");
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
                  required
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
              />
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
                placeholder="Lista de vacunas aplicadas..."
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