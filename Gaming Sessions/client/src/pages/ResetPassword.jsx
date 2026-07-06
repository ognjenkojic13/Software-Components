import { useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { resetPassword } from '../api/authApi';

export default function ResetPassword() {
  const [searchParams] = useSearchParams();
  const [token, setToken] = useState(searchParams.get('token') || '');
  const [newPassword, setNewPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await resetPassword(token, newPassword);
      setSuccess(true);
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setError(err.response?.data?.message || 'Reset lozinke nije uspeo');
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Reset lozinke</h2>
        {error && <div className="error">{error}</div>}
        {success ? (
          <p>Lozinka je uspesno promenjena.</p>
        ) : (
          <>
            <label>
              Token
              <input value={token} onChange={(e) => setToken(e.target.value)} required />
            </label>
            <label>
              Nova lozinka
              <input type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} required />
            </label>
            <button type="submit">Promeni lozinku</button>
          </>
        )}
        <div className="auth-links">
          <Link to="/login">Nazad na prijavu</Link>
        </div>
      </form>
    </div>
  );
}
