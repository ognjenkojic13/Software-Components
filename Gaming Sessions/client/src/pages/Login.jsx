import { useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { login as loginApi } from '../api/authApi';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const data = await loginApi(email, password);
      login(data.token);
      navigate(searchParams.get('returnUrl') || '/sessions');
    } catch (err) {
      setError(err.response?.data?.message || 'Prijava nije uspela');
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Prijava</h2>
        {error && <div className="error">{error}</div>}
        <label>
          Imejl
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </label>
        <label>
          Lozinka
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        </label>
        <button type="submit">Prijavi se</button>
        <div className="auth-links">
          <Link to="/register">Registracija</Link>
          <Link to="/forgot-password">Zaboravljena lozinka?</Link>
        </div>
      </form>
    </div>
  );
}
