import axiosClient from './axiosClient';

export const getMyProfile = () => axiosClient.get('/api/users/me').then((res) => res.data);

export const updateMyProfile = (data) => axiosClient.put('/api/users/me', data).then((res) => res.data);

export const listAllUsers = () => axiosClient.get('/api/users/admin').then((res) => res.data);

export const blockUser = (id) => axiosClient.put(`/api/users/admin/${id}/block`).then((res) => res.data);

export const unblockUser = (id) => axiosClient.put(`/api/users/admin/${id}/unblock`).then((res) => res.data);
