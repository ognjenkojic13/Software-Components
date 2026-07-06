import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function NavBar() {
  const { token, user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  if (!token) return null;

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">Gaming Sessions</div>
      <div className="navbar-links">
        <Link to="/sessions">Sesije</Link>
        <Link to="/profile">Profil</Link>
        <Link to="/notifications">Notifikacije</Link>
        {isAdmin && <Link to="/admin">Admin</Link>}
      </div>
      <div className="navbar-user">
        <span>{user?.username}</span>
        <button onClick={handleLogout}>Odjava</button>
      </div>
    </nav>
  );
}
