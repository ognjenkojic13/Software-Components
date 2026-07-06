import axiosClient from './axiosClient';

export const listGames = () => axiosClient.get('/api/games').then((res) => res.data);

export const createGame = (data) => axiosClient.post('/api/games', data).then((res) => res.data);

export const updateGame = (id, data) => axiosClient.put(`/api/games/${id}`, data).then((res) => res.data);
