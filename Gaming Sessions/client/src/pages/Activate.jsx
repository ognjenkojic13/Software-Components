import { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { activate } from '../api/authApi';

export default function Activate() {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState('loading');

  useEffect(() => {
    const token = searchParams.get('token');
    if (!token) {
      setStatus('error');
      return;
    }
    activate(token)
      .then(() => setStatus('success'))
      .catch(() => setStatus('error'));
  }, [searchParams]);

  return (
    <div className="auth-page">
      <div className="auth-form">
        <h2>Aktivacija naloga</h2>
        {status === 'loading' && <p>Aktivacija u toku...</p>}
        {status === 'success' && (
          <>
            <p>Nalog je uspesno aktiviran.</p>
            <Link to="/login">Prijavi se</Link>
          </>
        )}
        {status === 'error' && <p className="error">Aktivacija nije uspela - token je nevazeci ili istekao.</p>}
      </div>
    </div>
  );
}
