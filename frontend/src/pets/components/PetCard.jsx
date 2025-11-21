import { Link } from "react-router-dom";
import "../styles/petlist.css";

export default function PetCard({ pet, onDelete }) {
  return (
    <div className="pet-card">
      <h3>{pet.name}</h3>
      <p><strong>Especie:</strong> {pet.species}</p>

      <div className="card-actions">
        <Link to={`/pets/${pet.id}`}>Ver más</Link>
        <Link to={`/edit/${pet.id}`}>Editar</Link>
        <button onClick={() => onDelete(pet.id)}>Eliminar</button>
      </div>
    </div>
  );
}
