import { useState, useEffect } from "react";
import axios from "axios";
import "../styles/adoptante/adoptante.css";

function AdoptanteForm() {
  const [datosBasicos, setDatosBasicos] = useState({});
  const [form, setForm] = useState({
    phoneNumber: "",
    city: "",
    address: "",
    profilePicture: "",
    document: "",
    gender: "",
    otherGender: "",
    birthDate: "",
    monthlySalary: "",
    housingType: "",
    hasYard: false,
    petExperience: false,
    hasOtherPets: false,
    hasChildren: false,
    hoursAwayFromHome: "",
    hasAnimalExperience: false,
    preferredAnimalType: "",
    otherPreferredAnimalType: "",
    preferredPetSize: "",
    activityLevel: "",
    personalDescription: "",
  });
  const [acceptPolicy, setAcceptPolicy] = useState(false);
  const [activeTab, setActiveTab] = useState(1);

  useEffect(() => {
    const data = localStorage.getItem("datosBasicos");
    if (data) setDatosBasicos(JSON.parse(data));
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!acceptPolicy) {
      alert("Debes aceptar la política de tratamiento de datos para continuar.");
      return;
    }

    const adoptante = { ...datosBasicos, ...form };

    try {
      const response = await axios.post(
        "http://localhost:8181/api/petconnect/adoptantes/save",
        adoptante
      );
      alert("✅ Adoptante registrado con éxito!");
      console.log(response.data);
      localStorage.removeItem("datosBasicos");
      setForm({
        phoneNumber: "",
        city: "",
        address: "",
        profilePicture: "",
        document: "",
        gender: "",
        otherGender: "",
        birthDate: "",
        monthlySalary: "",
        housingType: "",
        hasYard: false,
        petExperience: false,
        hasOtherPets: false,
        hasChildren: false,
        hoursAwayFromHome: "",
        preferredAnimalType: "",
        otherPreferredAnimalType: "",
        preferredPetSize: "",
        activityLevel: "",
        personalDescription: "",
      });
      setAcceptPolicy(false);
    } catch (error) {
      console.error(error);
      alert("Error al registrar adoptante");
    }
  };

  const genders = ["FEMENINO", "MASCULINO", "PREFIERO_NO_DECIRLO", "OTRO"];
  const housingTypes = ["CASA", "APARTAMENTO", "FINCA"];
  const preferredAnimalTypes = ["PERRO", "GATO", "OTRO"];
  const preferredPetSizes = ["PEQUEÑA", "MEDIANA", "GRANDE"];
  const activityLevels = ["ALTO", "MEDIO", "BAJO"];

  const tabs = [
    { id: 1, title: "Personal"},
    { id: 2, title: "Vivienda"},
    { id: 3, title: "Experiencia"},
    { id: 4, title: "Preferencias"},
    { id: 5, title: "Finalizar"}
  ];

  const nextTab = () => setActiveTab(prev => Math.min(prev + 1, tabs.length));
  const prevTab = () => setActiveTab(prev => Math.max(prev - 1, 1));

  return (
    <div className="adoptante-container">
      <div className="adoptante-form-header">
        <h1>Completa tu Perfil de Adoptante</h1>
        <p>Ayúdanos a conocerte mejor para encontrar tu compañero perfecto</p>
      </div>

      <div className="adoptante-tabs">
        {tabs.map(tab => (
          <div
            key={tab.id}
            className={`adoptante-tab ${activeTab === tab.id ? 'active' : ''}`}
            onClick={() => setActiveTab(tab.id)}
          >
            {tab.icon} {tab.title}
          </div>
        ))}
      </div>

      <form onSubmit={handleSubmit}>
        {/* Pestaña 1: Información Personal */}
        <div className={`adoptante-tab-content ${activeTab === 1 ? 'active' : ''}`}>
          <div className="adoptante-form-section">
            <h2>Información Personal</h2>
            <div className="adoptante-form-grid">
              <div className="adoptante-input-group">
                <label>Teléfono *</label>
                <input
                  type="text"
                  name="phoneNumber"
                  placeholder="Ej: 300 123 4567"
                  value={form.phoneNumber}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="adoptante-input-group">
                <label>Ciudad *</label>
                <input
                  type="text"
                  name="city"
                  placeholder="Ej: Bogotá"
                  value={form.city}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="adoptante-input-group">
                <label>Dirección *</label>
                <input
                  type="text"
                  name="address"
                  placeholder="Ej: Calle 123 #45-67"
                  value={form.address}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="adoptante-input-group">
                <label>Documento *</label>
                <input
                  type="text"
                  name="document"
                  placeholder="Número de documento"
                  value={form.document}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="adoptante-input-group">
                <label>Género *</label>
                <select name="gender" value={form.gender} onChange={handleChange} required>
                  <option value="">Seleccionar género</option>
                  {genders.map((g) => (
                    <option key={g} value={g}>
                      {g.replace(/_/g, " ")}
                    </option>
                  ))}
                </select>
              </div>
              {form.gender === "OTRO" && (
                <div className="adoptante-input-group">
                  <label>Especificar género</label>
                  <input
                    type="text"
                    name="otherGender"
                    placeholder="Por favor especifica"
                    value={form.otherGender}
                    onChange={handleChange}
                  />
                </div>
              )}
              <div className="adoptante-input-group">
                <label>Fecha de Nacimiento *</label>
                <input 
                  type="date" 
                  name="birthDate" 
                  value={form.birthDate}
                  onChange={handleChange} 
                  required 
                />
              </div>
              <div className="adoptante-input-group">
                <label>Salario Mensual</label>
                <input
                  type="number"
                  name="monthlySalary"
                  placeholder="Ej: 2500000"
                  value={form.monthlySalary}
                  onChange={handleChange}
                />
              </div>
              <div className="adoptante-input-group">
                <label>Foto de Perfil (URL)</label>
                <input
                  type="text"
                  name="profilePicture"
                  placeholder="https://ejemplo.com/foto.jpg"
                  value={form.profilePicture}
                  onChange={handleChange}
                />
              </div>
            </div>
          </div>
        </div>

        {/* Pestaña 2: Información de Vivienda */}
        <div className={`adoptante-tab-content ${activeTab === 2 ? 'active' : ''}`}>
          <div className="adoptante-form-section">
            <h2>Información de Vivienda</h2>
            <div className="adoptante-form-grid">
              <div className="adoptante-input-group">
                <label>Tipo de Vivienda</label>
                <select name="housingType" value={form.housingType} onChange={handleChange}>
                  <option value="">Seleccionar tipo</option>
                  {housingTypes.map((h) => (
                    <option key={h} value={h}>
                      {h}
                    </option>
                  ))}
                </select>
              </div>
              <div className="adoptante-input-group">
                <label>Horas fuera de casa al día</label>
                <input
                  type="number"
                  name="hoursAwayFromHome"
                  placeholder="Ej: 8"
                  value={form.hoursAwayFromHome}
                  onChange={handleChange}
                  min="0"
                  max="24"
                />
              </div>
            </div>
            <div className="adoptante-checkbox-group">
              <label className="adoptante-checkbox-label">
                <input 
                  type="checkbox" 
                  name="hasYard" 
                  checked={form.hasYard} 
                  onChange={handleChange} 
                />
                <span className="adoptante-checkmark"></span>
                ¿Tiene patio o espacio exterior?
              </label>
              <label className="adoptante-checkbox-label">
                <input 
                  type="checkbox" 
                  name="hasChildren" 
                  checked={form.hasChildren} 
                  onChange={handleChange} 
                />
                <span className="adoptante-checkmark"></span>
                ¿Tiene hijos?
              </label>
            </div>
          </div>
        </div>

        {/* Pestaña 3: Experiencia con Mascotas */}
        <div className={`adoptante-tab-content ${activeTab === 3 ? 'active' : ''}`}>
          <div className="adoptante-form-section">
            <h2>Experiencia con Mascotas</h2>
            <div className="adoptante-checkbox-group">
              <label className="adoptante-checkbox-label">
                <input 
                  type="checkbox" 
                  name="petExperience" 
                  checked={form.petExperience} 
                  onChange={handleChange} 
                />
                <span className="adoptante-checkmark"></span>
                ¿Ha tenido mascotas antes?
              </label>
              <label className="adoptante-checkbox-label">
                <input 
                  type="checkbox" 
                  name="hasOtherPets" 
                  checked={form.hasOtherPets} 
                  onChange={handleChange} 
                />
                <span className="adoptante-checkmark"></span>
                ¿Tiene otras mascotas actualmente?
              </label>
            </div>
          </div>
        </div>

        {/* Pestaña 4: Preferencias de Adopción */}
        <div className={`adoptante-tab-content ${activeTab === 4 ? 'active' : ''}`}>
          <div className="adoptante-form-section">
            <h2>Preferencias de Adopción</h2>
            <div className="adoptante-form-grid">
              <div className="adoptante-input-group">
                <label>Tipo de Animal Preferido</label>
                <select name="preferredAnimalType" value={form.preferredAnimalType} onChange={handleChange}>
                  <option value="">Seleccionar tipo</option>
                  {preferredAnimalTypes.map((a) => (
                    <option key={a} value={a}>
                      {a}
                    </option>
                  ))}
                </select>
              </div>
              {form.preferredAnimalType === "OTRO" && (
                <div className="adoptante-input-group">
                  <label>Especificar tipo de animal</label>
                  <input
                    type="text"
                    name="otherPreferredAnimalType"
                    placeholder="Ej: Conejo, Ave, etc."
                    value={form.otherPreferredAnimalType}
                    onChange={handleChange}
                  />
                </div>
              )}
              <div className="adoptante-input-group">
                <label>Tamaño Preferido</label>
                <select name="preferredPetSize" value={form.preferredPetSize} onChange={handleChange}>
                  <option value="">Seleccionar tamaño</option>
                  {preferredPetSizes.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </select>
              </div>
              <div className="adoptante-input-group">
                <label>Nivel de Actividad</label>
                <select name="activityLevel" value={form.activityLevel} onChange={handleChange}>
                  <option value="">Seleccionar nivel</option>
                  {activityLevels.map((lvl) => (
                    <option key={lvl} value={lvl}>
                      {lvl}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          <div className="adoptante-form-section">
            <h2>Descripción Personal</h2>
            <div className="adoptante-input-group full-width">
              <label>Cuéntanos sobre ti y tu estilo de vida</label>
              <textarea
                name="personalDescription"
                placeholder="Describe tu rutina diaria, hobbies, personalidad, y por qué quieres adoptar una mascota..."
                value={form.personalDescription}
                onChange={handleChange}
                rows="4"
              />
            </div>
          </div>
        </div>

        {/* Pestaña 5: Finalizar */}
        <div className={`adoptante-tab-content ${activeTab === 5 ? 'active' : ''}`}>
          <div className="adoptante-form-section">
            <h2>Revisa tu Información</h2>
            <div className="adoptante-review-section">
              <div className="adoptante-review-group">
                <h3>Información Personal</h3>
                <p><strong>Teléfono:</strong> {form.phoneNumber || "No especificado"}</p>
                <p><strong>Ciudad:</strong> {form.city || "No especificado"}</p>
                <p><strong>Dirección:</strong> {form.address || "No especificado"}</p>
                <p><strong>Género:</strong> {form.gender ? form.gender.replace(/_/g, " ") : "No especificado"}</p>
              </div>
              
              <div className="adoptante-review-group">
                <h3>Vivienda y Experiencia</h3>
                <p><strong>Tipo de vivienda:</strong> {form.housingType || "No especificado"}</p>
                <p><strong>Horas fuera:</strong> {form.hoursAwayFromHome || "No especificado"}</p>
                <p><strong>Experiencia con mascotas:</strong> {form.petExperience ? "Sí" : "No"}</p>
                <p><strong>Otras mascotas:</strong> {form.hasOtherPets ? "Sí" : "No"}</p>
              </div>

              <div className="adoptante-review-group">
                <h3>Preferencias</h3>
                <p><strong>Animal preferido:</strong> {form.preferredAnimalType || "No especificado"}</p>
                <p><strong>Tamaño preferido:</strong> {form.preferredPetSize || "No especificado"}</p>
                <p><strong>Nivel de actividad:</strong> {form.activityLevel || "No especificado"}</p>
              </div>
            </div>

            <div className="adoptante-policy-section">
              <label className="adoptante-policy-checkbox">
                <input
                  type="checkbox"
                  checked={acceptPolicy}
                  onChange={(e) => setAcceptPolicy(e.target.checked)}
                  required
                />
                <span className="adoptante-policy-text">
                  Acepto la política de tratamiento de datos. Autorizo el uso de mi información
                  para el proceso de adopción y contactos relacionados con PetConnect.
                </span>
              </label>
            </div>
          </div>
        </div>

        {/* Navegación entre pestañas */}
        <div className="adoptante-tab-navigation">
          {activeTab > 1 && (
            <button type="button" className="adoptante-tab-btn" onClick={prevTab}>
              ← Anterior
            </button>
          )}
          
          {activeTab < tabs.length ? (
            <button type="button" className="adoptante-tab-btn primary" onClick={nextTab}>
              Siguiente →
            </button>
          ) : (
            <button
              type="submit"
              className={`adoptante-submit-btn ${!acceptPolicy ? "disabled" : ""}`}
              disabled={!acceptPolicy}
            >
              Completar Registro
            </button>
          )}
        </div>
      </form>
    </div>
  );
}

export default AdoptanteForm;