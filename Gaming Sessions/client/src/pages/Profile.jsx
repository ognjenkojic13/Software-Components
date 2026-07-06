import { useEffect, useState } from 'react';
import { getMyProfile, updateMyProfile } from '../api/usersApi';

export default function Profile() {
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState(null);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    getMyProfile().then((data) => {
      setProfile(data);
      setForm({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        dateOfBirth: data.dateOfBirth || ''
      });
    });
  }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    try {
      const updated = await updateMyProfile(form);
      setProfile(updated);
      setMessage('Profil je azuriran.');
    } catch (err) {
      setError(err.response?.data?.message || 'Azuriranje nije uspelo');
    }
  };

  if (!profile || !form) return <div className="page">Ucitavanje...</div>;

  return (
    <div className="page profile-page">
      <h2>Moj profil</h2>
      <div className="stats-grid">
        <div className="stat-card">
          <span className="stat-value">{profile.totalSessionsJoined}</span>
          <span className="stat-label">Prijavljenih sesija</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{profile.sessionsAttended}</span>
          <span className="stat-label">Posecenih sesija</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{profile.sessionsLeft}</span>
          <span className="stat-label">Napustanja</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{profile.attendancePercentage}%</span>
          <span className="stat-label">Procenat prisustva</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{profile.sessionsOrganized}</span>
          <span className="stat-label">Organizovanih sesija</span>
        </div>
        <div className="stat-card">
          <span className="stat-value">{profile.organizerTitle}</span>
          <span className="stat-label">Titula</span>
        </div>
      </div>

      <form className="card-form profile-edit-form" onSubmit={handleSubmit}>
        <h3>Uredi podatke</h3>
        {error && <div className="error">{error}</div>}
        {message && <div className="success">{message}</div>}
        <div className="form-grid">
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
            <input type="date" value={form.dateOfBirth} onChange={update('dateOfBirth')} />
          </label>
        </div>
        <button type="submit">Sacuvaj</button>
      </form>
    </div>
  );
}
