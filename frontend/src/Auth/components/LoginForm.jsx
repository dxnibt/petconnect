import { useState } from "react";
import axios from "axios";
import "../styles/register/style1.css"; 

function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

 const handleSubmit = async (e) => {
  e.preventDefault();
  console.log("Botón funcionando 🚀");
  
  try {
    const response = await axios.post("http://localhost:8181/api/petconnect/usuario/login", {
      email,
      password,
    });
    setMessage(response.data);
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