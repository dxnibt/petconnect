import { useEffect, useState } from "react";
import PetCard from "../components/PetCard";
import { getPets, deletePet } from "../../api/pets.js";
import "../styles/petlist.css";

export default function PetList() {
  const [pets, setPets] = useState([]);

  useEffect(() => {
    fetchPets();
  }, []);

  async function fetchPets() {
    const res = await getPets();
    setPets(res.data);
  }

  async function handleDelete(id) {
    if (confirm("¿Eliminar mascota?")) {
      await deletePet(id);
      setPets(pets.filter((p) => p.id !== id));
    }
  }

  return (
    <div className="pet-list-container">
      <h1>Lista de Mascotas</h1>

      <div className="pet-grid">
        {pets.length === 0 ? (
          <p>No hay mascotas registradas.</p>
        ) : (
          pets.map((pet) => (
            <PetCard key={pet.id} pet={pet} onDelete={handleDelete} />
          ))
        )}
      </div>
    </div>
  );
}
