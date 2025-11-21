import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createPet, getPet, updatePet } from "../api/pets";
import "../styles/petform.css";

export default function PetForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    species: "",
    age: "",
  });

  useEffect(() => {
    if (id) loadPet();
  }, [id]);

  async function loadPet() {
    const res = await getPet(id);
    setForm(res.data);
  }

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (id) {
      await updatePet(id, form);
    } else {
      await createPet(form);
    }

    navigate("/");
  }

  return (
    <div className="pet-form-container">
      <h1>{id ? "Editar Mascota" : "Crear Mascota"}</h1>

      <form onSubmit={handleSubmit} className="pet-form">
        <label>
          Nombre:
          <input
            name="name"
            value={form.name}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Especie:
          <input
            name="species"
            value={form.species}
            onChange={handleChange}
            required
          />
        </label>

        <label>
          Edad:
          <input
            name="age"
            type="number"
            value={form.age}
            onChange={handleChange}
          />
        </label>

        <button type="submit">{id ? "Actualizar" : "Crear"}</button>
      </form>
    </div>
  );
}
