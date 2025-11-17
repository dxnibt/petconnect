function RefugioForm({ datosBasicos }) {
  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Registro como REFUGIO completado para ${datosBasicos.name}`);
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1>Formulario de Refugio</h1>
      <p>Aquí irán los campos específicos del refugio</p>
      <button type="submit">Registrar Refugio</button>
    </form>
  );
}

// 👇 ESTA LÍNEA ES CLAVE
export default RefugioForm;
