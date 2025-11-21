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
  const [acceptPolicy, setAcceptPolicy] = useState(false); // Nuevo estado para políticas

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
      alert("❌ Error al registrar adoptante");
    }
  };

  // ENUMS exactos del backend
  const genders = ["FEMENINO", "MASCULINO", "PREFIERO_NO_DECIRLO", "OTRO"];
  const housingTypes = ["CASA", "APARTAMENTO", "FINCA"];
  const preferredAnimalTypes = ["PERRO", "GATO", "OTRO"];
  const preferredPetSizes = ["PEQUEÑA", "MEDIANA", "GRANDE"];
  const activityLevels = ["ALTO", "MEDIO", "BAJO"];

  return (
    <div className="adoptante-container">
      <div className="form-header">
        <h1>Completa tu Perfil de Adoptante</h1>
        <p>Ayúdanos a conocerte mejor para encontrar tu compañero perfecto</p>
      </div>
      
      <form onSubmit={handleSubmit}>
        <div className="form-section">
          <h2>Información Personal</h2>
          <div className="form-grid">
            <div className="input-group">
              <label>Teléfono *</label>
              <input type="text" name="phoneNumber" placeholder="Ej: +57 300 123 4567" onChange={handleChange} required />
            </div>
            <div className="input-group">
              <label>Ciudad *</label>
              <input type="text" name="city" placeholder="Ej: Bogotá" onChange={handleChange} required />
            </div>
            <div className="input-group">
              <label>Dirección *</label>
              <input type="text" name="address" placeholder="Ej: Calle 123 #45-67" onChange={handleChange} required />
            </div>
            <div className="input-group">
              <label>Documento *</label>
              <input type="text" name="document" placeholder="Número de documento" onChange={handleChange} required />
            </div>
            <div className="input-group">
              <label>Género *</label>
              <select name="gender" onChange={handleChange} required>
                <option value="">Seleccionar género</option>
                {genders.map((g) => <option key={g} value={g}>{g.replace(/_/g, ' ')}</option>)}
              </select>
            </div>
            {form.gender === "OTRO" && (
              <div className="input-group">
                <label>Especificar género</label>
                <input type="text" name="otherGender" placeholder="Por favor especifica" onChange={handleChange} />
              </div>
            )}
            <div className="input-group">
              <label>Fecha de Nacimiento *</label>
              <input type="date" name="birthDate" onChange={handleChange} required />
            </div>
            <div className="input-group">
              <label>Salario Mensual</label>
              <input type="number" name="monthlySalary" placeholder="Ej: 2500000" onChange={handleChange} />
            </div>
            <div className="input-group">
              <label>Foto de Perfil (URL)</label>
              <input type="text" name="profilePicture" placeholder="https://ejemplo.com/foto.jpg" onChange={handleChange} />
            </div>
          </div>
        </div>

        <div className="form-section">
          <h2>Información de Vivienda</h2>
          <div className="form-grid">
            <div className="input-group">
              <label>Tipo de Vivienda</label>
              <select name="housingType" onChange={handleChange}>
                <option value="">Seleccionar tipo</option>
                {housingTypes.map((h) => <option key={h} value={h}>{h}</option>)}
              </select>
            </div>
            <div className="input-group">
              <label>Horas fuera de casa al día</label>
              <input type="number" name="hoursAwayFromHome" placeholder="Ej: 8" onChange={handleChange} min="0" max="24" />
            </div>
          </div>
          <div className="checkbox-group">
            <label className="checkbox-label">
              <input type="checkbox" name="hasYard" onChange={handleChange} />
              <span className="checkmark"></span>
              ¿Tiene patio o espacio exterior?
            </label>
            <label className="checkbox-label">
              <input type="checkbox" name="hasChildren" onChange={handleChange} />
              <span className="checkmark"></span>
              ¿Tiene hijos?
            </label>
          </div>
        </div>

        <div className="form-section">
          <h2>Experiencia con Mascotas</h2>
          <div className="checkbox-group">
            <label className="checkbox-label">
              <input type="checkbox" name="petExperience" onChange={handleChange} />
              <span className="checkmark"></span>
              ¿Ha tenido mascotas antes?
            </label>
            <label className="checkbox-label">
              <input type="checkbox" name="hasOtherPets" onChange={handleChange} />
              <span className="checkmark"></span>
              ¿Tiene otras mascotas actualmente?
            </label>
          </div>
        </div>

        <div className="form-section">
          <h2>Preferencias de Adopción</h2>
          <div className="form-grid">
            <div className="input-group">
              <label>Tipo de Animal Preferido</label>
              <select name="preferredAnimalType" onChange={handleChange}>
                <option value="">Seleccionar tipo</option>
                {preferredAnimalTypes.map((a) => <option key={a} value={a}>{a}</option>)}
              </select>
            </div>
            {form.preferredAnimalType === "OTRO" && (
              <div className="input-group">
                <label>Especificar tipo de animal</label>
                <input type="text" name="otherPreferredAnimalType" placeholder="Ej: Conejo, Ave, etc." onChange={handleChange} />
              </div>
            )}
            <div className="input-group">
              <label>Tamaño Preferido</label>
              <select name="preferredPetSize" onChange={handleChange}>
                <option value="">Seleccionar tamaño</option>
                {preferredPetSizes.map((s) => <option key={s} value={s}>{s}</option>)}
              </select>
            </div>
            <div className="input-group">
              <label>Nivel de Actividad</label>
              <select name="activityLevel" onChange={handleChange}>
                <option value="">Seleccionar nivel</option>
                {activityLevels.map((lvl) => <option key={lvl} value={lvl}>{lvl}</option>)}
              </select>
            </div>
          </div>
        </div>

        <div className="form-section">
          <h2>Descripción Personal</h2>
          <div className="input-group">
            <label>Cuéntanos sobre ti y tu estilo de vida</label>
            <textarea 
              name="personalDescription" 
              placeholder="Describe tu rutina diaria, hobbies, personalidad, y por qué quieres adoptar una mascota..." 
              onChange={handleChange}
              rows="4"
            />
          </div>
        </div>

        {/* Política de tratamiento de datos */}
        <div className="policy-section">
          <label className="policy-checkbox">
            <input
              type="checkbox"
              checked={acceptPolicy}
              onChange={(e) => setAcceptPolicy(e.target.checked)}
              required
            />
            <span className="policy-text">
              Acepto la política de tratamiento de datos. Autorizo el uso de mi información 
              para el proceso de adopción y contactos relacionados con PetConnect.
            </span>
          </label>
        </div>

        <button 
          type="submit" 
          className={`submit-btn ${!acceptPolicy ? 'disabled' : ''}`}
          disabled={!acceptPolicy}
        >
          Completar Registro
        </button>
      </form>
    </div>
  );
}

export default AdoptanteForm;