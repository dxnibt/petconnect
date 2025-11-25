import { useState, useEffect } from "react";
import axios from "axios";
import "../styles/refugio/refugio.css";
import API_CONFIG from '../../config/api'

function RefugioForm() {
  const [datosBasicos, setDatosBasicos] = useState({});
  const [form, setForm] = useState({
    phoneNumber: "",
    city: "",
    address: "",
    profilePicture: "",
    nit: "",
    website: "",
    supportDocument: "",
    shelterDescription: ""
  });
  const [acceptedPolicy, setAcceptedPolicy] = useState(false);
  const [showPolicy, setShowPolicy] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const data = localStorage.getItem("datosBasicos");
    if (data) {
      try {
        const parsedData = JSON.parse(data);
        console.log("✅ Datos básicos cargados:", parsedData);
        setDatosBasicos(parsedData);
      } catch (error) {
        console.error("❌ Error parseando datos básicos:", error);
        localStorage.removeItem("datosBasicos");
      }
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const validateForm = () => {
    const requiredFields = [
      'phoneNumber', 'city', 'address', 'nit',
      'supportDocument', 'shelterDescription'
    ];

    const missingFields = requiredFields.filter(field => !form[field]?.trim());

    if (missingFields.length > 0) {
      alert(`Por favor completa los siguientes campos: ${missingFields.join(', ')}`);
      return false;
    }

    if (!acceptedPolicy) {
      alert("Debes aceptar la política de tratamiento de datos");
      return false;
    }

    // Verificar que datosBasicos tenga la información requerida
    if (!datosBasicos.name || !datosBasicos.email || !datosBasicos.password) {
      alert("Faltan datos básicos. Por favor completa el registro anterior.");
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      // Combinar correctamente según la estructura del backend
      const refugioData = {
        // Campos de datosBasicos
        name: datosBasicos.name,
        email: datosBasicos.email,
        password: datosBasicos.password,
        role: "REFUGIO", // Siempre será REFUGIO para este formulario

        // Campos del formulario actual
        phoneNumber: form.phoneNumber.trim(),
        city: form.city.trim(),
        address: form.address.trim(),
        profilePicture: form.profilePicture.trim() || "/default-profile.png",
        nit: form.nit.trim(),
        website: form.website.trim() || "",
        supportDocument: form.supportDocument.trim(),
        shelterDescription: form.shelterDescription.trim()
      };

      console.log("🚀 Datos completos a enviar:", refugioData);
      console.log("📤 JSON a enviar:", JSON.stringify(refugioData, null, 2));

      const response = await axios.post(
        `${API_CONFIG.AUTH_URL}/api/petconnect/refugios/save`,
        refugioData,
        {
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          },
          timeout: 10000
        }
      );

      console.log("✅ Respuesta del servidor:", response.data);

      alert("🎉 Refugio registrado con éxito!");

      // Limpiar todo
      localStorage.removeItem("datosBasicos");
      setForm({
        phoneNumber: "",
        city: "",
        address: "",
        profilePicture: "",
        nit: "",
        website: "",
        supportDocument: "",
        shelterDescription: ""
      });
      setAcceptedPolicy(false);
      setDatosBasicos({});

    } catch (error) {
      console.error("❌ Error completo:", error);

      if (error.response) {
        console.error("📊 Status:", error.response.status);
        console.error("📝 Datos del error:", error.response.data);

        let errorMessage = "Error al registrar refugio";

        if (error.response.status === 400) {
          if (error.response.data) {
            errorMessage = "Error en los datos:\n";
            if (typeof error.response.data === 'string') {
              errorMessage += error.response.data;
            } else if (typeof error.response.data === 'object') {
              // Mostrar errores específicos del backend
              Object.entries(error.response.data).forEach(([key, value]) => {
                errorMessage += `\n• ${key}: ${value}`;
              });
            }
          } else {
            errorMessage = "Datos inválidos. Verifica que todos los campos estén correctos.";
          }
        } else if (error.response.status === 409) {
          errorMessage = "El email o NIT ya están registrados";
        } else if (error.response.status === 500) {
          errorMessage = "Error interno del servidor";
        }

        alert(errorMessage);
      } else if (error.request) {
        alert("No se pudo conectar con el servidor. Verifica tu conexión.");
      } else {
        alert("Error: " + error.message);
      }
    } finally {
      setLoading(false);
    }
  };

  // Mostrar información de datos básicos cargados
  const renderDatosBasicosInfo = () => {
    if (!datosBasicos.name) return null;

    return (
      <div className="datos-basicos-info">
        <h3>Información de Usuario Cargada</h3>
        <div className="info-grid">
          <div className="info-item">
            <strong>Nombre:</strong> {datosBasicos.name}
          </div>
          <div className="info-item">
            <strong>Email:</strong> {datosBasicos.email}
          </div>
          <div className="info-item">
            <strong>Rol:</strong> REFUGIO
          </div>
        </div>
      </div>
    );
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
      <div className="form-wrapper">
        <div className="form-header">
          <h2>Registro de Refugio - Paso 2</h2>
          <p>Completa la información específica de tu refugio</p>
        </div>

        {renderDatosBasicosInfo()}

        <div className="form-container">
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label>Número de Teléfono <span className="required">*</span></label>
                <input
                  type="text"
                  name="phoneNumber"
                  value={form.phoneNumber}
                  onChange={handleChange}
                  placeholder="Ej: 3196129271"
                  required
                />
              </div>

              <div className="form-group">
                <label>Ciudad <span className="required">*</span></label>
                <input
                  type="text"
                  name="city"
                  value={form.city}
                  onChange={handleChange}
                  placeholder="Ej: Fusagasugá"
                  required
                />
              </div>

              <div className="form-group">
                <label>Dirección <span className="required">*</span></label>
                <input
                  type="text"
                  name="address"
                  value={form.address}
                  onChange={handleChange}
                  placeholder="Ej: Cra 7 este n23-34"
                  required
                />
              </div>

              <div className="form-group">
                <label>NIT <span className="required">*</span></label>
                <input
                  type="text"
                  name="nit"
                  value={form.nit}
                  onChange={handleChange}
                  placeholder="Número de identificación tributaria"
                  required
                />
              </div>

              <div className="form-group full-width">
                <label>Foto de Perfil (URL)</label>
                <input
                  type="url"
                  name="profilePicture"
                  value={form.profilePicture}
                  onChange={handleChange}
                  placeholder="https://ejemplo.com/foto.jpg o /ruta/local.png"
                />
              </div>

              <div className="form-group">
                <label>Sitio Web</label>
                <input
                  type="url"
                  name="website"
                  value={form.website}
                  onChange={handleChange}
                  placeholder="https://turefugio.com"
                />
              </div>

              <div className="form-group">
                <label>Documento de Soporte <span className="required">*</span></label>
                <input
                  type="text"
                  name="supportDocument"
                  value={form.supportDocument}
                  onChange={handleChange}
                  placeholder="Nombre del documento: documento.pdf"
                  required
                />
              </div>

              <div className="form-group full-width">
                <label>Descripción del Refugio <span className="required">*</span></label>
                <textarea
                  name="shelterDescription"
                  value={form.shelterDescription}
                  onChange={handleChange}
                  placeholder="Describe tu refugio, su misión, historia y los servicios que ofrece..."
                  rows="4"
                  required
                />
              </div>
            </div>

            {form.profilePicture && (
              <div className="image-preview-section">
                <label>Vista previa de la imagen:</label>
                <div className="image-preview">
                  <img
                    src={form.profilePicture}
                    alt="Vista previa del refugio"
                    onError={(e) => {
                      e.target.style.display = 'none';
                      e.target.nextSibling.style.display = 'block';
                    }}
                  />
                  <div className="image-error" style={{display: 'none'}}>
                    No se puede cargar la imagen
                  </div>
                </div>
              </div>
            )}

            <div className="policy-section">
              <div className="policy-header">
                <div className="policy-title">
                  <h3>Política de Tratamiento de Datos</h3>
                  <span className="required">*</span>
                </div>
                <button
                  type="button"
                  className="policy-toggle"
                  onClick={() => setShowPolicy(!showPolicy)}
                >
                  {showPolicy ? "▲ Ocultar" : "▼ Ver política completa"}
                </button>
              </div>

              {showPolicy && (
                <div className="policy-content">
                  <div className="policy-text">
                    {politicaTratamiento.split('\n').map((line, index) => (
                      <p key={index} className={line.includes('POLÍTICA') ? 'policy-main-title' :
                                              line.match(/^\d+\./) ? 'policy-subtitle' : 'policy-text-line'}>
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
                  <span className="checkbox-text">
                    He leído y acepto la política de tratamiento de datos personales
                  </span>
                </label>
              </div>
            </div>

            <button
              type="submit"
              className={`submit-btn ${!acceptedPolicy || loading ? "disabled" : ""}`}
              disabled={!acceptedPolicy || loading}
            >
              {loading ? (
                <>
                  <span className="loading-spinner"></span>
                  Registrando...
                </>
              ) : (
                <>
                  <span className="btn-text">Completar Registro</span>
                  <span className="btn-icon">🐾</span>
                </>
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default RefugioForm;