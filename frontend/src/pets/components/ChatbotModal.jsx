import { useState, useRef, useEffect } from "react";
import axios from "axios";
import "../styles/chatbot.css";

const ChatbotModal = ({ isOpen, onClose }) => {
  const [messages, setMessages] = useState([
    {
      text: "¡Hola! Soy tu asistente virtual de PetConnect. ¿En qué puedo ayudarte hoy?",
      isBot: true,
      timestamp: new Date(),
      formattedText: "¡Hola! Soy tu asistente virtual de PetConnect. ¿En qué puedo ayudarte hoy?"
    }
  ]);
  const [inputMessage, setInputMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [isSlowResponse, setIsSlowResponse] = useState(false);
  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // Función para formatear el texto del bot
  const formatBotResponse = (text) => {
    if (!text) return text;

    let formattedText = text;

    // Reemplazar **texto** por <strong>texto</strong>
    formattedText = formattedText.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

    // Reemplazar * por • para listas
    formattedText = formattedText.replace(/^\s*\*\s+/gm, '• ');

    // Reemplazar números con punto por lista numerada
    formattedText = formattedText.replace(/^\s*(\d+)\.\s+/gm, '<span class="list-number">$1.</span> ');

    // Separar secciones con ---
    formattedText = formattedText.replace(/---+/g, '<div class="section-divider"></div>');

    // Convertir saltos de línea en <br>
    formattedText = formattedText.replace(/\n/g, '<br>');

    // Detectar y formatear títulos
    formattedText = formattedText.replace(/^(.*?:)\s*<br>/g, '<div class="response-title">$1</div>');

    return formattedText;
  };

  // Función para detectar si el texto necesita formato especial
  const needsFormatting = (text) => {
    return text.includes('**') || text.includes('* ') || text.includes('1.') || text.includes('---');
  };

  // Timer para respuestas lentas
  useEffect(() => {
    let slowResponseTimer;
    
    if (isLoading) {
      slowResponseTimer = setTimeout(() => {
        setIsSlowResponse(true);
      }, 5000);
    } else {
      setIsSlowResponse(false);
    }

    return () => clearTimeout(slowResponseTimer);
  }, [isLoading]);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    
    if (!inputMessage.trim() || isLoading) return;

    // Agregar mensaje del usuario
    const userMessage = {
      text: inputMessage,
      isBot: false,
      timestamp: new Date(),
      formattedText: inputMessage
    };

    setMessages(prev => [...prev, userMessage]);
    const currentInput = inputMessage;
    setInputMessage("");
    setIsLoading(true);

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 30000);

    try {
      const token = localStorage.getItem("token");
      
      const requestBody = {
        message: currentInput
      };

      const response = await axios.post(
        "http://localhost:8282/api/petconnect/chatbot/send",
        requestBody,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          },
          signal: controller.signal,
          timeout: 25000
        }
      );

      clearTimeout(timeoutId);

      // Formatear la respuesta del bot
      const botResponse = response.data;
      const formattedResponse = needsFormatting(botResponse) 
        ? formatBotResponse(botResponse)
        : botResponse;

      // Agregar respuesta del bot
      const botMessage = {
        text: botResponse,
        isBot: true,
        timestamp: new Date(),
        formattedText: formattedResponse,
        needsFormatting: needsFormatting(botResponse)
      };

      setMessages(prev => [...prev, botMessage]);
      
    } catch (error) {
      clearTimeout(timeoutId);
      console.error("Error al enviar mensaje:", error);
      
      let errorText = "Lo siento, hubo un error al procesar tu mensaje.";
      
      if (error.code === 'ECONNABORTED' || error.name === 'AbortError') {
        errorText = "La respuesta está tomando demasiado tiempo. Por favor, intenta con una pregunta más específica o vuelve a intentarlo.";
      } else if (error.response) {
        errorText = `Error del servidor: ${error.response.status}`;
      }

      const errorMessage = {
        text: errorText,
        isBot: true,
        timestamp: new Date(),
        formattedText: errorText
      };

      setMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsLoading(false);
      setIsSlowResponse(false);
    }
  };

  const formatTime = (date) => {
    return date.toLocaleTimeString('es-ES', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage(e);
    }
  };

  const clearChat = () => {
    setMessages([
      {
        text: "¡Hola! Soy tu asistente virtual de PetConnect. ¿En qué puedo ayudarte hoy?",
        isBot: true,
        timestamp: new Date(),
        formattedText: "¡Hola! Soy tu asistente virtual de PetConnect. ¿En qué puedo ayudarte hoy?"
      }
    ]);
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="chatbot-modal">
        <div className="chatbot-header">
          <div className="chatbot-title">
            <span className="chatbot-icon">🤖</span>
            <h3>Asistente Virtual</h3>
          </div>
          <div className="chatbot-actions">
            <button 
              className="clear-btn"
              onClick={clearChat}
              title="Limpiar conversación"
            >
              🗑️
            </button>
            <button className="close-btn" onClick={onClose}>×</button>
          </div>
        </div>

        <div className="chatbot-messages">
          {messages.map((message, index) => (
            <div
              key={index}
              className={`message ${message.isBot ? 'bot-message' : 'user-message'}`}
            >
              <div className="message-content">
                {message.needsFormatting ? (
                  <div 
                    className="formatted-response"
                    dangerouslySetInnerHTML={{ __html: message.formattedText }}
                  />
                ) : (
                  <p>{message.text}</p>
                )}
                <span className="message-time">
                  {formatTime(message.timestamp)}
                </span>
              </div>
            </div>
          ))}
          
          {isLoading && (
            <>
              <div className="message bot-message">
                <div className="message-content">
                  <div className="typing-indicator">
                    <span></span>
                    <span></span>
                    <span></span>
                  </div>
                </div>
              </div>
              
              {isSlowResponse && (
                <div className="message bot-message">
                  <div className="message-content slow-response-message">
                    <p>⏳ El asistente está procesando tu pregunta. Esto puede tomar unos segundos...</p>
                  </div>
                </div>
              )}
            </>
          )}
          
          <div ref={messagesEndRef} />
        </div>

        <form className="chatbot-input-form" onSubmit={handleSendMessage}>
          <div className="input-container">
            <input
              type="text"
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="Escribe tu mensaje..."
              disabled={isLoading}
              autoFocus
            />
            <button 
              type="submit" 
              disabled={!inputMessage.trim() || isLoading}
              className="send-btn"
            >
              {isLoading ? "⏳" : "➤"}
            </button>
          </div>
          {isLoading && (
            <div className="loading-info">
              Procesando tu mensaje...
            </div>
          )}
        </form>
      </div>
    </div>
  );
};

export default ChatbotModal;