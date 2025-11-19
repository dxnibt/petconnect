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
      <h1>Registro de Adoptante</h1>
      <form onSubmit={handleSubmit}>
        <input type="text" name="phoneNumber" placeholder="Teléfono" onChange={handleChange} required />
        <input type="text" name="city" placeholder="Ciudad" onChange={handleChange} required />
        <input type="text" name="address" placeholder="Dirección" onChange={handleChange} required />
        <input type="text" name="profilePicture" placeholder="URL de foto de perfil" onChange={handleChange} />
        <input type="text" name="document" placeholder="Documento" onChange={handleChange} required />

        <select name="gender" onChange={handleChange} required>
          <option value="">Seleccionar género</option>
          {genders.map((g) => <option key={g} value={g}>{g}</option>)}
        </select>
        <input type="text" name="otherGender" placeholder="Otro género (si aplica)" onChange={handleChange} />
        <label>Fecha de nacimiento</label>
        <input type="date" name="birthDate" onChange={handleChange} required />

        <input type="number" name="monthlySalary" placeholder="Salario mensual" onChange={handleChange} />

        <select name="housingType" onChange={handleChange}>
          <option value="">Tipo de vivienda</option>
          {housingTypes.map((h) => <option key={h} value={h}>{h}</option>)}
        </select>

        <label><input type="checkbox" name="hasYard" onChange={handleChange} /> ¿Tiene patio?</label>
        <label><input type="checkbox" name="petExperience" onChange={handleChange} /> ¿Tiene experiencia con mascotas?</label>
        <label><input type="checkbox" name="hasOtherPets" onChange={handleChange} /> ¿Tiene otras mascotas?</label>
        <label><input type="checkbox" name="hasChildren" onChange={handleChange} /> ¿Tiene hijos?</label>

        <input type="number" name="hoursAwayFromHome" placeholder="Horas fuera de casa" onChange={handleChange} />

        <select name="preferredAnimalType" onChange={handleChange}>
          <option value="">Tipo de animal preferido</option>
          {preferredAnimalTypes.map((a) => <option key={a} value={a}>{a}</option>)}
        </select>
        <input type="text" name="otherPreferredAnimalType" placeholder="Otro tipo preferido (si aplica)" onChange={handleChange} />

        <select name="preferredPetSize" onChange={handleChange}>
          <option value="">Tamaño de mascota preferido</option>
          {preferredPetSizes.map((s) => <option key={s} value={s}>{s}</option>)}
        </select>

        <select name="activityLevel" onChange={handleChange}>
          <option value="">Nivel de actividad</option>
          {activityLevels.map((lvl) => <option key={lvl} value={lvl}>{lvl}</option>)}
        </select>

        <textarea name="personalDescription" placeholder="Descripción personal" onChange={handleChange} />

        <button type="submit">Registrar Adoptante</button>
      </form>
    </div>
  );
}

export default AdoptanteForm;
