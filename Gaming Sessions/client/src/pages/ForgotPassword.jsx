import { useState } from 'react';
import { Link } from 'react-router-dom';
import { forgotPassword } from '../api/authApi';

export default function ForgotPassword() {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    await forgotPassword(email);
    setSent(true);
  };

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Zaboravljena lozinka</h2>
        {sent ? (
          <p>Ako nalog postoji, poslat je imejl sa uputstvom za reset lozinke.</p>
        ) : (
          <>
            <label>
              Imejl
              <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            </label>
            <button type="submit">Posalji zahtev</button>
          </>
        )}
        <div className="auth-links">
          <Link to="/reset-password">Imate token za reset?</Link>
          <Link to="/login">Nazad na prijavu</Link>
        </div>
      </form>
    </div>
  );
}
