import { Link } from "react-router-dom";
import "../styles/layout.css";

export default function NavBar() {
  return (
    <nav className="nav-container">
      <h2>Mascotas</h2>
      <div className="nav-links">
        <Link to="/">Lista</Link>
        <Link to="/new">Crear</Link>
      </div>
    </nav>
  );
}
