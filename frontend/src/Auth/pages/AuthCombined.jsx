import { useState } from "react";
import LoginForm from "../components/LoginForm";
import RegisterForm from "../components/RegisterForm";
import "../styles/register/style1.css";

export default function AuthCombined() {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <div className="auth-combined-container">
      <div className="auth-toggle">
        <button
          className={isLogin ? "active" : ""}
          onClick={() => setIsLogin(true)}
        >
          Iniciar Sesión
        </button>
        <button
          className={!isLogin ? "active" : ""}
          onClick={() => setIsLogin(false)}
        >
          Registrarse
        </button>
      </div>

      <div className="auth-form">
        {isLogin ? <LoginForm /> : <RegisterForm />}
      </div>
    </div>
  );
}
