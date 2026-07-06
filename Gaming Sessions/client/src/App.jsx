import { Navigate, Route, Routes } from 'react-router-dom';
import NavBar from './components/NavBar';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import Activate from './pages/Activate';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';
import Profile from './pages/Profile';
import Sessions from './pages/Sessions';
import SessionDetail from './pages/SessionDetail';
import AcceptInvitation from './pages/AcceptInvitation';
import Notifications from './pages/Notifications';
import Admin from './pages/Admin';

export default function App() {
  return (
    <>
      <NavBar />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/activate" element={<Activate />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/invitations/accept" element={<AcceptInvitation />} />

        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <Profile />
            </ProtectedRoute>
          }
        />
        <Route
          path="/sessions"
          element={
            <ProtectedRoute>
              <Sessions />
            </ProtectedRoute>
          }
        />
        <Route
          path="/sessions/:id"
          element={
            <ProtectedRoute>
              <SessionDetail />
            </ProtectedRoute>
          }
        />
        <Route
          path="/notifications"
          element={
            <ProtectedRoute>
              <Notifications />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin"
          element={
            <ProtectedRoute adminOnly>
              <Admin />
            </ProtectedRoute>
          }
        />

        <Route path="/" element={<Navigate to="/sessions" replace />} />
        <Route path="*" element={<Navigate to="/sessions" replace />} />
      </Routes>
    </>
  );
}
