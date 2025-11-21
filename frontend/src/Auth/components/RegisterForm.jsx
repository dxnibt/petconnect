import { useState } from "react";
import axios from "axios";

function RegisterForm() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "adoptante",
  });
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:8181/api/petconnect/usuario/register",
        formData
      );

      setMessage("Registro exitoso 🎉 Ahora puedes iniciar sesión.");
      console.log("✅ Usuario registrado:", response.data);
    } catch (error) {
      console.error("❌ Error al registrar usuario:", error);
      if (error.response) {
        setMessage(error.response.data.message || "Error al registrarse");
      } else {
        setMessage("Error al conectar con el servidor");
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form-content">
      <h2>Registrarse</h2>

      <input
        type="text"
        name="name"
        placeholder="Nombre completo"
        value={formData.name}
        onChange={handleChange}
        required
      />

      <input
        type="email"
        name="email"
        placeholder="Correo electrónico"
        value={formData.email}
        onChange={handleChange}
        required
      />

      <input
        type="password"
        name="password"
        placeholder="Contraseña"
        value={formData.password}
        onChange={handleChange}
        required
      />

      <select
        name="role"
        value={formData.role}
        onChange={handleChange}
        required
      >
        <option value="adoptante">Adoptante</option>
        <option value="refugio">Refugio</option>
      </select>

      <button type="submit">Registrarse</button>
      {message && <p className="message">{message}</p>}
    </form>
  );
}

export default RegisterForm;
