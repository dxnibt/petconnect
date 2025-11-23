const API_CONFIG = {
  development: {
    AUTH_URL: 'http://localhost:8181',
    PETS_URL: 'http://localhost:9494',
    CHATBOT_URL: 'http://localhost:9090',
    NOTIFICATION_URL: 'http://localhost:9092'
  },
  production: {
    AUTH_URL: 'https://auth-service-cgdc.onrender.com',
    PETS_URL: 'https://pets-service-gbdc.onrender.com',
    CHATBOT_URL: 'https://chatbot-service-59lk.onrender.com',
    NOTIFICATION_URL: 'https://notification-service.onrender.com'
  }
};

export default API_CONFIG[process.env.NODE_ENV || 'development'];