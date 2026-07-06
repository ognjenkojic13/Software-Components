import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { searchSessions, createSession } from '../api/sessionsApi';
import { listGames } from '../api/gamesApi';

const emptyForm = {
  title: '',
  gameId: '',
  maxPlayers: 5,
  sessionType: 'OPEN',
  startDateTime: '',
  description: ''
};

export default function Sessions() {
  const [sessions, setSessions] = useState([]);
  const [games, setGames] = useState([]);
  const [filters, setFilters] = useState({ gameId: '', sessionType: '', description: '', joinedOnly: false, sortBy: 'startTime' });
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [form, setForm] = useState(emptyForm);
  const [error, setError] = useState('');

  const loadGames = () => listGames().then(setGames);

  const loadSessions = () => {
    const params = {};
    if (filters.gameId) params.gameId = filters.gameId;
    if (filters.sessionType) params.sessionType = filters.sessionType;
    if (filters.description) params.description = filters.description;
    if (filters.joinedOnly) params.joinedOnly = true;
    if (filters.sortBy) params.sortBy = filters.sortBy;
    return searchSessions(params).then(setSessions);
  };

  useEffect(() => {
    loadGames();
  }, []);

  useEffect(() => {
    loadSessions();
  }, [filters]);

  const updateFilter = (field) => (e) =>
    setFilters({ ...filters, [field]: e.target.type === 'checkbox' ? e.target.checked : e.target.value });

  const updateForm = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleCreate = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await createSession({ ...form, gameId: Number(form.gameId), maxPlayers: Number(form.maxPlayers) });
      setForm(emptyForm);
      setShowCreateForm(false);
      loadSessions();
    } catch (err) {
      setError(err.response?.data?.message || 'Kreiranje sesije nije uspelo');
    }
  };

  return (
    <div className="page">
      <div className="page-header">
        <h2>Gejming sesije</h2>
        <button onClick={() => setShowCreateForm((v) => !v)}>
          {showCreateForm ? 'Otkazi' : 'Nova sesija'}
        </button>
      </div>

      {showCreateForm && (
        <form className="card-form" onSubmit={handleCreate}>
          {error && <div className="error">{error}</div>}
          <label>
            Naziv
            <input value={form.title} onChange={updateForm('title')} required />
          </label>
          <label>
            Igra
            <select value={form.gameId} onChange={updateForm('gameId')} required>
              <option value="">-- izaberi igru --</option>
              {games.map((g) => (
                <option key={g.id} value={g.id}>
                  {g.name}
                </option>
              ))}
            </select>
          </label>
          <label>
            Maksimalan broj igraca
            <input type="number" min="1" value={form.maxPlayers} onChange={updateForm('maxPlayers')} required />
          </label>
          <label>
            Tip sesije
            <select value={form.sessionType} onChange={updateForm('sessionType')}>
              <option value="OPEN">Otvorena</option>
              <option value="CLOSED">Zatvorena</option>
            </select>
          </label>
          <label>
            Datum i vreme pocetka
            <input type="datetime-local" value={form.startDateTime} onChange={updateForm('startDateTime')} required />
          </label>
          <label>
            Opis
            <textarea value={form.description} onChange={updateForm('description')} />
          </label>
          <button type="submit">Kreiraj sesiju</button>
        </form>
      )}

      <div className="filters">
        <select value={filters.gameId} onChange={updateFilter('gameId')}>
          <option value="">Sve igre</option>
          {games.map((g) => (
            <option key={g.id} value={g.id}>
              {g.name}
            </option>
          ))}
        </select>
        <select value={filters.sessionType} onChange={updateFilter('sessionType')}>
          <option value="">Svi tipovi</option>
          <option value="OPEN">Otvorena</option>
          <option value="CLOSED">Zatvorena</option>
        </select>
        <input placeholder="Pretraga po opisu" value={filters.description} onChange={updateFilter('description')} />
        <label className="checkbox-label">
          <input type="checkbox" checked={filters.joinedOnly} onChange={updateFilter('joinedOnly')} />
          Samo moje prijave
        </label>
        <select value={filters.sortBy} onChange={updateFilter('sortBy')}>
          <option value="startTime">Sortiraj po vremenu</option>
          <option value="participants">Sortiraj po broju prijavljenih</option>
        </select>
      </div>

      <div className="session-list">
        {sessions.map((s) => (
          <Link className="session-card" to={`/sessions/${s.id}`} key={s.id}>
            <div className="session-card-title">{s.title}</div>
            <div className="session-card-meta">
              {s.game.name} - {s.sessionType === 'OPEN' ? 'Otvorena' : 'Zatvorena'} - {s.status}
            </div>
            <div className="session-card-meta">
              {new Date(s.startDateTime).toLocaleString()} | {s.currentPlayers}/{s.maxPlayers} igraca
            </div>
            {s.joinedByCurrentUser && <span className="badge">Prijavljeni ste</span>}
          </Link>
        ))}
        {sessions.length === 0 && <p>Nema sesija za zadate filtere.</p>}
      </div>
    </div>
  );
}
