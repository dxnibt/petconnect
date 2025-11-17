import { useState } from "react";
import "../styles/register/style1.css";
import "../styles/register/style2.css";
import "../styles/register/style3.css";
import "../styles/register/style4.css";
import "../styles/register/style5.css";
import RegisterForm from "../components/RegisterForm";

export default function RegisterPage() {
  const [active, setActive] = useState(false); // controla si se muestra el registro o el login

  return (
    <div className={`container ${active ? "active" : ""}`} id="container">
      {/* Formulario de Registro */}
      <div className="form-container sign-up">
        <RegisterForm />
      </div>

      {/* Formulario de Inicio de Sesión */}
      <div className="form-container sign-in">
        <form>
          <h1>Iniciar Sesión</h1>
          <input type="email" placeholder="Correo electrónico" required />
          <input type="password" placeholder="Contraseña" required />
          <a href="#">¿Olvidaste tu contraseña?</a>
          <button type="button">Iniciar sesión</button>
        </form>
      </div>

      {/* Panel de alternancia */}
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