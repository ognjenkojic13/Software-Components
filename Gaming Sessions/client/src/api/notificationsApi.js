import axiosClient from './axiosClient';

export const listMyNotifications = () => axiosClient.get('/api/notifications').then((res) => res.data);

export const listNotificationTypes = () => axiosClient.get('/api/notification-types').then((res) => res.data);

export const createNotificationType = (data) =>
  axiosClient.post('/api/notification-types', data).then((res) => res.data);

export const updateNotificationType = (id, data) =>
  axiosClient.put(`/api/notification-types/${id}`, data).then((res) => res.data);
