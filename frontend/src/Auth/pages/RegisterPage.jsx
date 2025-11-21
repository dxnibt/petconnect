import { useState } from "react";
import RegisterForm from "../components/RegisterForm.jsx";
import "../styles/register/style1.css";

export default function RegisterPage() {
  const [active, setActive] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    // Aquí va la lógica de login que estaba en LoginForm
    const email = e.target.email.value;
    const password = e.target.password.value;

    try {
      const response = await fetch("http://localhost:8181/api/petconnect/usuario/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        
        // Guardar en localStorage
        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);
        localStorage.setItem("userEmail", email);
        localStorage.setItem("isLoggedIn", "true");
        
        alert("Inicio de sesión exitoso ✅");
        
        // Redirigir a home
        window.location.href = "/";
      } else {
        const error = await response.text();
        alert(error || "Error en el inicio de sesión");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al conectar con el servidor");
    }
  };

  return (
    <div className={`container ${active ? "active" : ""}`} id="container">
      {/* Panel de Registro */}
      <div className="form-container sign-up">
        <RegisterForm />
      </div>

      {/* Panel de Login - Ahora integrado directamente */}
      <div className="form-container sign-in">
        <form onSubmit={handleLogin}>
          <h1>Iniciar Sesión</h1>
          <input 
            type="email" 
            name="email"
            placeholder="Correo electrónico" 
            required 
          />
          <input 
            type="password" 
            name="password"
            placeholder="Contraseña" 
            required 
          />
          <a href="#">¿Olvidaste tu contraseña?</a>
          <button type="submit">Iniciar sesión</button>
        </form>
      </div>

      <div className="toggle-container">
        <div className="toggle">
          <div className="toggle-panel toggle-left">
            <h1>Bienvenido de nuevo!</h1>
            <p>
              Ingresa tus datos personales para utilizar todas las funciones de
              PetConnect
            </p>
            <button className="hidden" id="login" onClick={() => setActive(false)}>
              Iniciar sesión
            </button>
          </div>

          <div className="toggle-panel toggle-right">
            <h1>Hola!</h1>
            <p>
              Regístrate con tus datos personales para utilizar todas las
              funciones de PetConnect
            </p>
            <button className="hidden" id="register" onClick={() => setActive(true)}>
              Registrarse
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}