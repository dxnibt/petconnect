import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getPet } from "../../api/pets";
import "../styles/petdetails.css";

export default function PetDetails() {
  const { id } = useParams();
  const [pet, setPet] = useState(null);

  useEffect(() => {
    loadPet();
  }, []);

  async function loadPet() {
    const res = await getPet(id);
    setPet(res.data);
  }

  if (!pet) return <p>Cargando...</p>;

  return (
    <div className="pet-details-container">
      <h1>{pet.name}</h1>

      <p><strong>Especie:</strong> {pet.species}</p>
      <p><strong>Edad:</strong> {pet.age}</p>

      <Link to={`/edit/${pet.id}`} className="edit-btn">Editar</Link>
    </div>
  );
}
