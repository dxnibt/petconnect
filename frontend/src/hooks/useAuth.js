import { useState, useEffect } from 'react';

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userRole, setUserRole] = useState(null);
  const [userEmail, setUserEmail] = useState('');
  const [userName, setUserName] = useState(''); // 👈 NUEVO

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const email = localStorage.getItem('userEmail');
    const name = localStorage.getItem('userName'); // 👈 NUEVO
    const loggedIn = localStorage.getItem('isLoggedIn');

    if (token && loggedIn === 'true') {
      setIsAuthenticated(true);
      setUserRole(role);
      setUserEmail(email || '');
      setUserName(name || ''); // 👈 NUEVO
    }
  }, []);

  const login = (token, role, email, name = '') => { // 👈 ACTUALIZADO
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    localStorage.setItem('userEmail', email);
    localStorage.setItem('userName', name); // 👈 NUEVO
    localStorage.setItem('isLoggedIn', 'true');
    setIsAuthenticated(true);
    setUserRole(role);
    setUserEmail(email);
    setUserName(name); // 👈 NUEVO
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName'); // 👈 NUEVO
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('datosBasicos');
    setIsAuthenticated(false);
    setUserRole(null);
    setUserEmail('');
    setUserName(''); // 👈 NUEVO
  };

  return {
    isAuthenticated,
    userRole,
    userEmail,
    userName, // 👈 NUEVO
    login,
    logout
  };
};