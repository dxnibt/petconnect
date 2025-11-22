import { useState } from "react";
import axios from "axios";
import "../styles/adoption.css";

export default function AdoptionModal({ mascota, isOpen, onClose, onSuccess }) {
  const [loading, setLoading] = useState(false);
  const [confirmText, setConfirmText] = useState("");

  if (!isOpen || !mascota) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (confirmText.toLowerCase() !== "confirmar") {
      alert("Por favor escribe 'CONFIRMAR' para proceder con la adopción");
      return;
    }

    setLoading(true);
    try {
      const token = localStorage.getItem("token");

      // Obtener el ID de la mascota
      const petId = mascota.pet_id || mascota.id || mascota.mascotaId;

      const adoptionData = {
        petId: petId
      };

      console.log("📤 Enviando solicitud de adopción:", adoptionData);

      const response = await axios.post(
        "http://localhost:9494/api/petconnect/adopciones/save",
        adoptionData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      console.log("Solicitud enviada exitosamente:", response.data);
      alert("¡Solicitud de adopción enviada exitosamente!\n\nTu solicitud está EN PROCESO. Te contactaremos pronto.");
      onSuccess();
      onClose();
      
    } catch (error) {
      console.error("Error al enviar solicitud:", error);
      console.log("Detalles del error:", error.response?.data);
      
      if (error.response?.status === 400) {
        const errorMessage = error.response?.data?.message || "Datos inválidos";
        
        if (errorMessage.includes("shelterId") || errorMessage.includes("no está permitido")) {
 
          await tryAlternativeStructure(mascota);
        } else {
          alert("Error: " + errorMessage);
        }
      } else if (error.response?.status === 409) {
        alert("Ya tienes una solicitud de adopción pendiente para esta mascota.");
      } else if (error.response?.status === 401) {
        alert("Sesión expirada. Por favor, inicia sesión nuevamente.");
      } else if (error.response?.status === 500) {
        alert("Error del servidor. Por favor, intenta nuevamente más tarde.");
      } else {
        alert("Error al enviar la solicitud: " + (error.response?.data?.message || error.message));
      }
    } finally {
      setLoading(false);
    }
  };

  const tryAlternativeStructure = async (mascota) => {
    try {
      const token = localStorage.getItem("token");
      const petId = mascota.pet_id || mascota.id || mascota.mascotaId;

      const adoptionData = petId;

      console.log("🔄 Probando estructura alternativa:", adoptionData);

      const response = await axios.post(
        "http://localhost:9494/api/petconnect/adopciones/save",
        adoptionData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      console.log("Solicitud enviada exitosamente (estructura alternativa):", response.data);
      alert("¡Solicitud de adopción enviada exitosamente!\n\nTu solicitud está EN PROCESO. Te contactaremos pronto.");
      onSuccess();
      onClose();

    } catch (secondError) {
      console.error("Error con estructura alternativa:", secondError);
      
      await trySpanishStructure(mascota);
    }
  };

  const trySpanishStructure = async (mascota) => {
    try {
      const token = localStorage.getItem("token");
      const petId = mascota.pet_id || mascota.id || mascota.mascotaId;

      const adoptionData = {
        mascotaId: petId,
        estado: "EN_PROCESO"
      };

      console.log("🔄 Probando estructura en español:", adoptionData);

      const response = await axios.post(
        "http://localhost:9494/api/petconnect/adopciones/save",
        adoptionData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );

      console.log("Solicitud enviada exitosamente (español):", response.data);
      alert("¡Solicitud de adopción enviada exitosamente!\n\nTu solicitud está EN PROCESO. Te contactaremos pronto.");
      onSuccess();
      onClose();

    } catch (thirdError) {
      console.error("Error con estructura en español:", thirdError);
      
      alert("No se pudo enviar la solicitud. Por favor, contacta al administrador.\n\nError: " + 
            (thirdError.response?.data?.message || "Estructura de datos no compatible"));
    }
  };

  return (
    <div className="modal-overlay">
      <div className="adoption-modal">
        <div className="modal-header">
          <h2>Confirmar Solicitud de Adopción</h2>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>
        
        <div className="modal-content">
          <div className="pet-info">
            <img 
              src={mascota.imageUrl || mascota.imagenUrl || "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&h=200&fit=crop"} 
              alt={mascota.name || mascota.nombre}
              onError={(e) => {
                e.target.src = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&h=200&fit=crop";
              }}
            />
            <div className="pet-details">
              <h3>{mascota.name || mascota.nombre}</h3>
              <p>
                {mascota.species === 'PERRO' ? '🐕 Perro' : 
                 mascota.species === 'GATO' ? '🐈 Gato' : 
                 '🐾 ' + (mascota.otherspecies || mascota.especie || 'Otra especie')}
                {mascota.race && ` • ${mascota.race}`}
                {mascota.raza && ` • ${mascota.raza}`}
              </p>
              <p className="pet-age">
                {mascota.age && `Edad: ${mascota.age} años`}
                {mascota.edad && `Edad: ${mascota.edad} años`}
              </p>
            </div>
          </div>

          <div className="adoption-process">
            <h4>📋 Proceso de Adopción</h4>
            <div className="process-steps">
              <div className="step">
                <span className="step-number">1</span>
                <span className="step-text">Solicitud enviada (EN_PROCESO)</span>
              </div>
              <div className="step">
                <span className="step-number">2</span>
                <span className="step-text">Revisión por el refugio</span>
              </div>
              <div className="step">
                <span className="step-number">3</span>
                <span className="step-text">Entrevista y visita</span>
              </div>
              <div className="step">
                <span className="step-number">4</span>
                <span className="step-text">Resultado: ACEPTADA o RECHAZADA</span>
              </div>
            </div>
          </div>

          <div className="adoption-warning">
            <h4>⚠️ Compromiso de Adopción</h4>
            <ul>
              <li>La adopción es un compromiso de por vida con la mascota</li>
              <li>Debes proveer alimento, vivienda y cuidados veterinarios</li>
              <li>Te contactaremos para una entrevista de seguimiento</li>
              <li>El refugio puede verificar las condiciones del hogar</li>
              <li>Puede haber un período de prueba antes de la adopción definitiva</li>
            </ul>
          </div>

          <form onSubmit={handleSubmit} className="adoption-form">
            <div className="form-group">
              <label htmlFor="confirmText">
                Para confirmar, escribe <strong>"CONFIRMAR"</strong> en el siguiente campo:
              </label>
              <input
                type="text"
                id="confirmText"
                value={confirmText}
                onChange={(e) => setConfirmText(e.target.value)}
                placeholder="Escribe CONFIRMAR aquí..."
                required
                disabled={loading}
                autoComplete="off"
              />
              <small className="form-help">
                Esto asegura que realmente quieres proceder con la adopción
              </small>
            </div>

            <div className="modal-actions">
              <button 
                type="button" 
                className="cancel-btn"
                onClick={onClose}
                disabled={loading}
              >
                Cancelar
              </button>
              <button 
                type="submit" 
                className="confirm-btn"
                disabled={loading || confirmText.toLowerCase() !== "confirmar"}
              >
                {loading ? (
                  <>
                    <div className="loading-spinner-small"></div>
                    Probando diferentes formatos...
                  </>
                ) : (
                  "🎉 Enviar Solicitud de Adopción"
                )}
              </button>
            </div>

            <div className="form-footer">
              <p>
                <strong>Estado inicial:</strong> EN_PROCESO<br/>
                <strong>Próximo paso:</strong> Revisión por el refugio<br/>
                <strong>Nota:</strong> El sistema detecta automáticamente tu usuario y el refugio
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}