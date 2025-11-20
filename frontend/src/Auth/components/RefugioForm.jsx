import { useState, useEffect } from "react";
import axios from "axios";
import "../styles/refugio/refugio.css";

function RefugioForm() {
  const [datosBasicos, setDatosBasicos] = useState({});
  const [form, setForm] = useState({
    nit: "",
    website: "",
    supportDocument: "",
    shelterDescription: ""
  });

  useEffect(() => {
    const data = localStorage.getItem("datosBasicos");
    if (data) setDatosBasicos(JSON.parse(data));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const refugio = { ...datosBasicos, ...form };

    try {
      const response = await axios.post(
        "http://localhost:8181/api/petconnect/refugios/save",
        refugio
      );
      alert("Refugio registrado con éxito!");
      localStorage.removeItem("datosBasicos");
      setForm({ nit: "", website: "", supportDocument: "", shelterDescription: "" });
    } catch (error) {
      console.error(error);
      alert("Error al registrar refugio");
    }
  };

  return (
    <div className="form-container">
      <h2>Registro de Refugio</h2>
      <form onSubmit={handleSubmit}>
        <label>NIT</label>
        <input type="text" name="nit" value={form.nit} onChange={handleChange} required />

        <label>Sitio Web</label>
        <input type="text" name="website" value={form.website} onChange={handleChange} />

        <label>Documento de Soporte</label>
        <input type="text" name="supportDocument" value={form.supportDocument} onChange={handleChange} required />

        <label>Descripción del Refugio</label>
        <textarea name="shelterDescription" value={form.shelterDescription} onChange={handleChange} required />

        <button type="submit">Registrar Refugio</button>
      </form>
    </div>
  );
}

export default RefugioForm;
