import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate(); // 👈 este hook sirve para navegar

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:8080/api/petconnect/usuario/login",
        { email, password }
      );

      // Si el login fue exitoso:
      localStorage.setItem("token", response.data.token);
      localStorage.setItem("role", response.data.role);

      setMessage("Inicio de sesión exitoso ✅");

      // 🔹 Redirige a la página principal
      navigate("/"); // 👈 aquí cambias la ruta
    } catch (error) {
      if (error.response) {
        setMessage(error.response.data);
      } else {
        setMessage("Error al conectar con el servidor");
      }
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
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
    </div>
  );
}

export default LoginForm;