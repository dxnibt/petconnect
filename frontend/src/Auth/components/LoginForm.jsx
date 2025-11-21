import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:8181/api/petconnect/usuario/login",
        { email, password }
      );

      // ✅ Guardar token y rol en localStorage
      localStorage.setItem("token", response.data.token);
      localStorage.setItem("role", response.data.role);
      setMessage("Inicio de sesión exitoso ✅");

      // ✅ Redirigir al Home después de un momento
      setTimeout(() => navigate("/"), 800);
    } catch (error) {
      console.error("❌ Error al iniciar sesión:", error);
      if (error.response) {
        setMessage(error.response.data.message || "Credenciales inválidas");
      } else {
        setMessage("Error al conectar con el servidor");
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className="auth-form-content">
      <h2>Iniciar sesión</h2>

      <input
        type="email"
        placeholder="Correo electrónico"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />

      <input
        type="password"
        placeholder="Contraseña"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />

      <button type="submit">Ingresar</button>
      {message && <p className="message">{message}</p>}
    </form>
  );
}

export default LoginForm;
