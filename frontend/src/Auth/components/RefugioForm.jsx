import { useState, useEffect } from "react";
import axios from "axios";
import "../styles/refugio/refugio.css";

function RefugioForm() {
  const [datosBasicos, setDatosBasicos] = useState({});
  const [form, setForm] = useState({
    nit: "",
    website: "",
    supportDocument: "",
    shelterDescription: ""
  });
  const [acceptedPolicy, setAcceptedPolicy] = useState(false);
  const [showPolicy, setShowPolicy] = useState(false);

  useEffect(() => {
    const data = localStorage.getItem("datosBasicos");
    if (data) setDatosBasicos(JSON.parse(data));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!acceptedPolicy) {
      alert("Debes aceptar la política de tratamiento de datos para continuar");
      return;
    }

    const refugio = { ...datosBasicos, ...form };

    try {
      const response = await axios.post(
        "http://localhost:8181/api/petconnect/refugios/save",
        refugio
      );
      alert("Refugio registrado con éxito!");
      localStorage.removeItem("datosBasicos");
      setForm({ nit: "", website: "", supportDocument: "", shelterDescription: "" });
      setAcceptedPolicy(false);
    } catch (error) {
      console.error(error);
      alert("Error al registrar refugio");
    }
  };

  const politicaTratamiento = `
    POLÍTICA DE TRATAMIENTO DE DATOS PERSONALES - PETCONNECT

    1. FINALIDAD: Los datos personales recopilados serán utilizados exclusivamente para:
       - Gestión y administración del registro del refugio
       - Comunicación relacionada con servicios de adopción
       - Envío de información relevante sobre el bienestar animal
       - Cumplimiento de obligaciones legales

    2. DERECHOS: Como titular de los datos usted tiene derecho a:
       - Conocer, actualizar y rectificar sus datos
       - Solicitar prueba de la autorización otorgada
       - Revocar la autorización y/o solicitar la supresión del dato
       - Acceder en forma gratuita a sus datos personales

    3. VIGENCIA: Los datos personales serán tratados por el tiempo necesario para cumplir con las finalidades mencionadas y según lo dispuesto por la normatividad vigente.

    4. SEGURIDAD: Implementamos medidas técnicas y administrativas para proteger sus datos contra acceso no autorizado, pérdida o destrucción.

    5. CONTACTO: Para ejercer sus derechos, puede contactarnos a través de: proteccion.datos@petconnect.org

    Al aceptar esta política, autoriza expresamente el tratamiento de sus datos personales de acuerdo con lo aquí establecido.
  `;

  return (
    <div className="refugio-container">
      <div className="form-container">
        <h2>Registro de Refugio</h2>
        <form onSubmit={handleSubmit}>
          <label>NIT</label>
          <input type="text" name="nit" value={form.nit} onChange={handleChange} required />

          <label>Sitio Web</label>
          <input type="text" name="website" value={form.website} onChange={handleChange} />

          <label>Documento de Soporte</label>
          <input type="text" name="supportDocument" value={form.supportDocument} onChange={handleChange} required />

          <label>Descripción del Refugio</label>
          <textarea name="shelterDescription" value={form.shelterDescription} onChange={handleChange} required />

          {/* Sección de Política de Tratamiento de Datos */}
          <div className="policy-section">
            <div className="policy-header">
              <h3>Política de Tratamiento de Datos</h3>
              <button 
                type="button" 
                className="policy-toggle"
                onClick={() => setShowPolicy(!showPolicy)}
              >
                {showPolicy ? "▲ Ocultar" : "▼ Ver política"}
              </button>
            </div>
            
            {showPolicy && (
              <div className="policy-content">
                <div className="policy-text">
                  {politicaTratamiento.split('\n').map((line, index) => (
                    <p key={index} className={line.includes('POLÍTICA') ? 'policy-title' : ''}>
                      {line}
                    </p>
                  ))}
                </div>
              </div>
            )}

            <div className="policy-acceptance">
              <label className="checkbox-label">
                <input
                  type="checkbox"
                  checked={acceptedPolicy}
                  onChange={(e) => setAcceptedPolicy(e.target.checked)}
                  required
                />
                <span className="checkmark"></span>
                Acepto la política de tratamiento de datos personales
              </label>
            </div>
          </div>

          <button type="submit" className={!acceptedPolicy ? "disabled" : ""}>
            Registrar Refugio
          </button>
        </form>
      </div>
    </div>
  );
}

export default RefugioForm;
