import axiosClient from './axiosClient';

export const login = (email, password) =>
  axiosClient.post('/api/users/login', { email, password }).then((res) => res.data);

export const register = (data) => axiosClient.post('/api/users/register', data).then((res) => res.data);

export const activate = (token) => axiosClient.get('/api/users/activate', { params: { token } }).then((res) => res.data);

export const forgotPassword = (email) =>
  axiosClient.post('/api/users/forgot-password', { email }).then((res) => res.data);

export const resetPassword = (token, newPassword) =>
  axiosClient.post('/api/users/reset-password', { token, newPassword }).then((res) => res.data);
