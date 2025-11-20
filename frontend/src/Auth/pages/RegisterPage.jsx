import { useState } from "react";
import RegisterForm from "../components/RegisterForm.jsx";
import LoginForm from "../components/LoginForm.jsx";
import "../styles/register/style1.css";

export default function RegisterPage() {
  const [active, setActive] = useState(false);

  return (
    <div className={`container ${active ? "active" : ""}`} id="container">
      <div className="form-container sign-up">
        <RegisterForm />
      </div>

      <div className="form-container sign-in">
        <form>
          <h1>Iniciar Sesión</h1>
          <input type="email" placeholder="Correo electrónico" required />
          <input type="password" placeholder="Contraseña" required />
          <a href="#">¿Olvidaste tu contraseña?</a>
          <button type="button">Iniciar sesión</button>
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