import { useEffect, useState } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { joinSession } from '../api/sessionsApi';
import { useAuth } from '../context/AuthContext';

export default function AcceptInvitation() {
  const [searchParams] = useSearchParams();
  const sessionId = searchParams.get('sessionId');
  const token = searchParams.get('token');
  const { token: authToken } = useAuth();
  const navigate = useNavigate();
  const [status, setStatus] = useState('loading');

  useEffect(() => {
    if (!authToken) {
      const returnUrl = `/invitations/accept?sessionId=${sessionId}&token=${token}`;
      navigate(`/login?returnUrl=${encodeURIComponent(returnUrl)}`, { replace: true });
      return;
    }
    if (!sessionId || !token) {
      setStatus('error');
      return;
    }
    joinSession(sessionId, token)
      .then(() => {
        setStatus('success');
        setTimeout(() => navigate(`/sessions/${sessionId}`), 1500);
      })
      .catch(() => setStatus('error'));
  }, [authToken, sessionId, token, navigate]);

  return (
    <div className="auth-page">
      <div className="auth-form">
        <h2>Prihvatanje pozivnice</h2>
        {status === 'loading' && <p>Obrada pozivnice u toku...</p>}
        {status === 'success' && <p>Uspesno ste prijavljeni na sesiju. Preusmeravanje...</p>}
        {status === 'error' && (
          <>
            <p className="error">Pozivnica nije vazeca ili je vec iskoriscena.</p>
            <Link to="/sessions">Nazad na sesije</Link>
          </>
        )}
      </div>
    </div>
  );
}
