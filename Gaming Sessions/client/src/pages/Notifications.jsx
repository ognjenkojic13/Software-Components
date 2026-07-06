import { useEffect, useState } from 'react';
import { listMyNotifications } from '../api/notificationsApi';

export default function Notifications() {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    listMyNotifications().then(setNotifications);
  }, []);

  return (
    <div className="page">
      <h2>Notifikacije</h2>
      <div className="session-list">
        {notifications.map((n) => (
          <div className="session-card" key={n.id}>
            <div className="session-card-title">{n.subject}</div>
            <div className="session-card-meta">{n.typeCode} - {n.status}</div>
            <p>{n.body}</p>
            <div className="session-card-meta">{new Date(n.createdAt).toLocaleString()}</div>
          </div>
        ))}
        {notifications.length === 0 && <p>Nema notifikacija.</p>}
      </div>
    </div>
  );
}
