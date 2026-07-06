import axiosClient from './axiosClient';

export const searchSessions = (params) => axiosClient.get('/api/sessions', { params }).then((res) => res.data);

export const getSessionDetail = (id) => axiosClient.get(`/api/sessions/${id}`).then((res) => res.data);

export const createSession = (data) => axiosClient.post('/api/sessions', data).then((res) => res.data);

export const joinSession = (id, invitationToken) =>
  axiosClient
    .post(`/api/sessions/${id}/join`, null, { params: invitationToken ? { invitationToken } : {} })
    .then((res) => res.data);

export const inviteToSession = (id, email) =>
  axiosClient.post(`/api/sessions/${id}/invite`, { email }).then((res) => res.data);

export const cancelSession = (id) => axiosClient.put(`/api/sessions/${id}/cancel`).then((res) => res.data);

export const concludeSession = (id, attendees) =>
  axiosClient.put(`/api/sessions/${id}/conclude`, { attendees }).then((res) => res.data);
