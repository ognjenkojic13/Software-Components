import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { register } from '../api/authApi';

const emptyForm = { username: '', password: '', firstName: '', lastName: '', email: '', dateOfBirth: '' };

export default function Register() {
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await register(form);
      setSuccess(true);
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Registracija nije uspela');
    }
  };

  if (success) {
    return (
      <div className="auth-page">
        <div className="auth-form">
          <h2>Uspesna registracija</h2>
          <p>Aktivacioni imejl je poslat. Prijava je moguca nakon aktivacije naloga.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Registracija</h2>
        {error && <div className="error">{error}</div>}
        <label>
          Korisnicko ime
          <input value={form.username} onChange={update('username')} required />
        </label>
        <label>
          Lozinka
          <input type="password" value={form.password} onChange={update('password')} required />
        </label>
        <label>
          Ime
          <input value={form.firstName} onChange={update('firstName')} required />
        </label>
        <label>
          Prezime
          <input value={form.lastName} onChange={update('lastName')} required />
        </label>
        <label>
          Imejl
          <input type="email" value={form.email} onChange={update('email')} required />
        </label>
        <label>
          Datum rodjenja
          <input type="date" value={form.dateOfBirth} onChange={update('dateOfBirth')} required />
        </label>
        <button type="submit">Registruj se</button>
        <div className="auth-links">
          <Link to="/login">Vec imate nalog? Prijavite se</Link>
        </div>
      </form>
    </div>
  );
}
