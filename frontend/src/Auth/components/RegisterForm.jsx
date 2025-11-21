import { useState } from "react";
import { useNavigate } from "react-router-dom";

function RegisterForm() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    role: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!form.role) {
      alert("Por favor selecciona un rol antes de continuar.");
      return;
    }

    localStorage.setItem("datosBasicos", JSON.stringify(form));

    if (form.role === "ADOPTANTE") {
      navigate("/adoptante");
    } else if (form.role === "REFUGIO") {
      navigate("/refugio");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Crear Cuenta</h1>
      <input
        type="text"
        name="name"
        placeholder="Nombre"
        onChange={handleChange}
        required
      />
      <input
        type="email"
        name="email"
        placeholder="Correo electrónico"
        onChange={handleChange}
        required
      />
      <input
        type="password"
        name="password"
        placeholder="Contraseña"
        onChange={handleChange}
        required
      />
      <select name="role" onChange={handleChange} required>
        <option value="">Seleccionar Rol</option>
        <option value="ADOPTANTE">Adoptante</option>
        <option value="REFUGIO">Refugio</option>
      </select>

      <button type="submit">Continuar</button>
    </form>
  );
}

export default RegisterForm;