import { createContext, useContext, useState } from 'react';
import { decodeJwt } from '../utils/jwt';

const AuthContext = createContext(null);

function buildUserFromToken(token) {
  if (!token) return null;
  const claims = decodeJwt(token);
  if (!claims) return null;
  return { id: claims.sub, username: claims.username, role: claims.role };
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(buildUserFromToken(localStorage.getItem('token')));

  const login = (newToken) => {
    localStorage.setItem('token', newToken);
    setToken(newToken);
    setUser(buildUserFromToken(newToken));
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, login, logout, isAdmin: user?.role === 'ADMIN' }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
