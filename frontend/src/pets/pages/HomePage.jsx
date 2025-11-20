// src/pages/HomePage.jsx
import { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import "../styles/home.css";

export default function HomePage() {
  const [mascotas, setMascotas] = useState([]);
  const [page, setPage] = useState(0);
  const size = 4; // cantidad de mascotas por página
  const [totalPages, setTotalPages] = useState(1);

  useEffect(() => {
  const fetchMascotas = async () => {
    try {
      const response = await axios.get(
        `http://localhost:9494/api/petconnect/mascotas/List`
      );
      // Si tu API devuelve directamente un array:
      setMascotas(response.data); 
    } catch (error) {
      console.error("Error al cargar mascotas:", error);
    }
  };
  fetchMascotas();
}, [page]);


  return (
    <div className="home-container">
      <header>
        <h1>PetConnect</h1>
        <div className="auth-buttons">
          <Link to="/register"><button>Sign Up/Sign In</button></Link>

        </div>
      </header>

      <main>
        <h2>Catálogo de Mascotas</h2>
        <div className="mascotas-grid">
          {mascotas.length > 0 ? (
            mascotas.map((mascota) => (
              <div key={mascota.id} className="mascota-card">
                <img src={mascota.imageUrl} alt={mascota.name} />
                <h3>{mascota.name}</h3>
                <p>{mascota.description}</p>
              </div>
            ))
          ) : (
            <p>No hay mascotas disponibles.</p>
          )}
        </div>

        <div className="pagination">
          <button disabled={page === 0} onClick={() => setPage(page - 1)}>Anterior</button>
          <span>Página {page + 1} de {totalPages}</span>
          <button disabled={page + 1 >= totalPages} onClick={() => setPage(page + 1)}>Siguiente</button>
        </div>
      </main>
    </div>
  );
}
