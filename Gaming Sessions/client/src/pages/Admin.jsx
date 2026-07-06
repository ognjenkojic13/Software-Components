import { useEffect, useState } from 'react';
import { listAllUsers, blockUser, unblockUser } from '../api/usersApi';
import { listGames, createGame, updateGame } from '../api/gamesApi';
import { listMyNotifications, listNotificationTypes, createNotificationType, updateNotificationType } from '../api/notificationsApi';

function UsersTab() {
  const [users, setUsers] = useState([]);

  const load = () => listAllUsers().then(setUsers);
  useEffect(() => {
    load();
  }, []);

  const toggleBlock = async (u) => {
    if (u.blocked) await unblockUser(u.id);
    else await blockUser(u.id);
    load();
  };

  return (
    <table className="admin-table">
      <thead>
        <tr>
          <th>Korisnicko ime</th>
          <th>Imejl</th>
          <th>Uloga</th>
          <th>Prisustvo %</th>
          <th>Titula</th>
          <th>Status</th>
          <th />
        </tr>
      </thead>
      <tbody>
        {users.map((u) => (
          <tr key={u.id}>
            <td>{u.username}</td>
            <td>{u.email}</td>
            <td>{u.role}</td>
            <td>{u.attendancePercentage}</td>
            <td>{u.organizerTitle}</td>
            <td>{u.blocked ? 'Blokiran' : 'Aktivan'}</td>
            <td>
              {u.role !== 'ADMIN' && (
                <button onClick={() => toggleBlock(u)}>{u.blocked ? 'Odblokiraj' : 'Blokiraj'}</button>
              )}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

function GamesTab() {
  const [games, setGames] = useState([]);
  const [form, setForm] = useState({ name: '', description: '', genre: '' });
  const [editingId, setEditingId] = useState(null);

  const load = () => listGames().then(setGames);
  useEffect(() => {
    load();
  }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (editingId) await updateGame(editingId, form);
    else await createGame(form);
    setForm({ name: '', description: '', genre: '' });
    setEditingId(null);
    load();
  };

  const edit = (g) => {
    setEditingId(g.id);
    setForm({ name: g.name, description: g.description || '', genre: g.genre || '' });
  };

  return (
    <div>
      <form className="card-form" onSubmit={handleSubmit}>
        <h3>{editingId ? 'Izmeni igru' : 'Nova igra'}</h3>
        <label>
          Naziv
          <input value={form.name} onChange={update('name')} required />
        </label>
        <label>
          Opis
          <input value={form.description} onChange={update('description')} />
        </label>
        <label>
          Zanr
          <input value={form.genre} onChange={update('genre')} />
        </label>
        <button type="submit">{editingId ? 'Sacuvaj' : 'Dodaj'}</button>
      </form>
      <table className="admin-table">
        <thead>
          <tr>
            <th>Naziv</th>
            <th>Opis</th>
            <th>Zanr</th>
            <th />
          </tr>
        </thead>
        <tbody>
          {games.map((g) => (
            <tr key={g.id}>
              <td>{g.name}</td>
              <td>{g.description}</td>
              <td>{g.genre}</td>
              <td>
                <button onClick={() => edit(g)}>Izmeni</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function NotificationTypesTab() {
  const [types, setTypes] = useState([]);
  const [form, setForm] = useState({ code: '', description: '', subjectTemplate: '', bodyTemplate: '' });
  const [editingId, setEditingId] = useState(null);

  const load = () => listNotificationTypes().then(setTypes);
  useEffect(() => {
    load();
  }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (editingId) await updateNotificationType(editingId, form);
    else await createNotificationType(form);
    setForm({ code: '', description: '', subjectTemplate: '', bodyTemplate: '' });
    setEditingId(null);
    load();
  };

  const edit = (t) => {
    setEditingId(t.id);
    setForm({ code: t.code, description: t.description, subjectTemplate: t.subjectTemplate, bodyTemplate: t.bodyTemplate });
  };

  return (
    <div>
      <form className="card-form" onSubmit={handleSubmit}>
        <h3>{editingId ? 'Izmeni tip notifikacije' : 'Novi tip notifikacije'}</h3>
        <label>
          Kod
          <input value={form.code} onChange={update('code')} required disabled={Boolean(editingId)} />
        </label>
        <label>
          Opis
          <input value={form.description} onChange={update('description')} required />
        </label>
        <label>
          Sablon naslova
          <input value={form.subjectTemplate} onChange={update('subjectTemplate')} required />
        </label>
        <label>
          Sablon tela poruke
          <textarea value={form.bodyTemplate} onChange={update('bodyTemplate')} required />
        </label>
        <button type="submit">{editingId ? 'Sacuvaj' : 'Dodaj'}</button>
      </form>
      <table className="admin-table">
        <thead>
          <tr>
            <th>Kod</th>
            <th>Opis</th>
            <th />
          </tr>
        </thead>
        <tbody>
          {types.map((t) => (
            <tr key={t.id}>
              <td>{t.code}</td>
              <td>{t.description}</td>
              <td>
                <button onClick={() => edit(t)}>Izmeni</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function AllNotificationsTab() {
  const [notifications, setNotifications] = useState([]);
  useEffect(() => {
    listMyNotifications().then(setNotifications);
  }, []);

  return (
    <table className="admin-table">
      <thead>
        <tr>
          <th>Primalac</th>
          <th>Tip</th>
          <th>Naslov</th>
          <th>Status</th>
          <th>Datum</th>
        </tr>
      </thead>
      <tbody>
        {notifications.map((n) => (
          <tr key={n.id}>
            <td>{n.recipientEmail}</td>
            <td>{n.typeCode}</td>
            <td>{n.subject}</td>
            <td>{n.status}</td>
            <td>{new Date(n.createdAt).toLocaleString()}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

const TABS = [
  { key: 'users', label: 'Korisnici', Component: UsersTab },
  { key: 'games', label: 'Igre', Component: GamesTab },
  { key: 'notification-types', label: 'Tipovi notifikacija', Component: NotificationTypesTab },
  { key: 'notifications', label: 'Sve notifikacije', Component: AllNotificationsTab }
];

export default function Admin() {
  const [activeTab, setActiveTab] = useState('users');
  const ActiveComponent = TABS.find((t) => t.key === activeTab).Component;

  return (
    <div className="page">
      <h2>Admin panel</h2>
      <div className="tabs">
        {TABS.map((t) => (
          <button
            key={t.key}
            className={activeTab === t.key ? 'tab active' : 'tab'}
            onClick={() => setActiveTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>
      <ActiveComponent />
    </div>
  );
}
