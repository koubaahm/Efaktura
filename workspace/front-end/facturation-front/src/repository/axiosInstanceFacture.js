import axios from 'axios';
import { getToken } from '../components/sharedDataLocalStorage/TokenStorage';

const apiFact = axios.create({
    baseURL: 'http://localhost:8888/FACTURATION-SERVICE',
    withCredentials: false,
    headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
        'Access-Control-Allow-Headers': '*',
        'Content-Type': 'application/json',
    },
});


apiFact.interceptors.request.use(
    (config) => {
        const token = getToken();

        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default apiFact;