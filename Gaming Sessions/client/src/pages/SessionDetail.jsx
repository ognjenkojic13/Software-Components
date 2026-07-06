import { useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import {
  getSessionDetail,
  joinSession,
  inviteToSession,
  cancelSession,
  concludeSession
} from '../api/sessionsApi';
import { useAuth } from '../context/AuthContext';

export default function SessionDetail() {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const invitationToken = searchParams.get('invitationToken');
  const { user, isAdmin } = useAuth();

  const [session, setSession] = useState(null);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [inviteEmail, setInviteEmail] = useState('');
  const [attendance, setAttendance] = useState({});

  const load = () => getSessionDetail(id).then(setSession);

  useEffect(() => {
    load();
  }, [id]);

  if (!session) return <div className="page">Ucitavanje...</div>;

  const isOrganizer = String(session.organizerId) === String(user?.id);
  const alreadyJoined = session.participants.some((p) => String(p.userId) === String(user?.id));

  const runAction = async (action, successMessage) => {
    setError('');
    setMessage('');
    try {
      await action();
      setMessage(successMessage);
      load();
    } catch (err) {
      setError(err.response?.data?.message || 'Akcija nije uspela');
    }
  };

  const handleJoin = () => runAction(() => joinSession(id, invitationToken), 'Uspesno ste se prijavili.');

  const handleInvite = (e) => {
    e.preventDefault();
    runAction(() => inviteToSession(id, inviteEmail), 'Pozivnica je poslata.');
    setInviteEmail('');
  };

  const handleCancel = () => runAction(() => cancelSession(id), 'Sesija je otkazana.');

  const handleConclude = (e) => {
    e.preventDefault();
    const attendees = session.participants.map((p) => ({
      userId: p.userId,
      attended: Boolean(attendance[p.userId])
    }));
    runAction(() => concludeSession(id, attendees), 'Sesija je zakljucena.');
  };

  return (
    <div className="page">
      <h2>{session.title}</h2>
      <p className="session-card-meta">
        {session.game.name} - {session.sessionType === 'OPEN' ? 'Otvorena' : 'Zatvorena'} - {session.status}
      </p>
      <p>{session.description}</p>
      <p>
        Organizator: {session.organizerUsername} | Pocetak: {new Date(session.startDateTime).toLocaleString()} |
        Max igraca: {session.maxPlayers}
      </p>

      {error && <div className="error">{error}</div>}
      {message && <div className="success">{message}</div>}

      {session.status === 'SCHEDULED' && !isOrganizer && !alreadyJoined && (
        <button onClick={handleJoin}>Prijavi se na sesiju</button>
      )}

      <h3>Ucesnici ({session.participants.length})</h3>
      <ul>
        {session.participants.map((p) => (
          <li key={p.userId}>
            {p.username}
            {p.attended !== null && p.attended !== undefined && (p.attended ? ' - prisustvovao' : ' - nije prisustvovao')}
          </li>
        ))}
      </ul>

      {isOrganizer && session.status === 'SCHEDULED' && (
        <>
          <form className="card-form" onSubmit={handleInvite}>
            <h3>Pozovi igraca (zatvorena sesija)</h3>
            <label>
              Imejl
              <input type="email" value={inviteEmail} onChange={(e) => setInviteEmail(e.target.value)} required />
            </label>
            <button type="submit">Posalji pozivnicu</button>
          </form>

          <form className="card-form" onSubmit={handleConclude}>
            <h3>Zakljuci sesiju - evidencija prisustva</h3>
            {session.participants.map((p) => (
              <label className="checkbox-label" key={p.userId}>
                <input
                  type="checkbox"
                  checked={Boolean(attendance[p.userId])}
                  onChange={(e) => setAttendance({ ...attendance, [p.userId]: e.target.checked })}
                />
                {p.username} prisustvovao/la
              </label>
            ))}
            <button type="submit">Zakljuci sesiju</button>
          </form>
        </>
      )}

      {(isOrganizer || isAdmin) && session.status === 'SCHEDULED' && (
        <button className="danger" onClick={handleCancel}>
          Otkazi sesiju
        </button>
      )}
    </div>
  );
}
