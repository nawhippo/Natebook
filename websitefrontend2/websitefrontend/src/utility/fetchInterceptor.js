import Cookies from 'js-cookie';
import { showSessionExpiredOverlay } from './sessionExpiredOverlay';

const backendBaseUrl = 'https://natebook.onrender.com';
const fetchWithJWT = async (endpoint, options = {}) => {
    const token = Cookies.get('jwt')?.trim();


    const headers = {
        ...options.headers,
    };


    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    const url = `${backendBaseUrl}${endpoint}`;

    try {
        const response = await fetch(url, { ...options, headers });

        if (!response.ok) {
            const responseBody = await response.json();
            console.error('Response not ok:', responseBody);
            if (responseBody.message && responseBody.message.includes('JWT expired')) {
                showSessionExpiredOverlay();
                return Promise.reject('Session expired');
            }
            throw new Error('Response was not ok!');
        }

        return response;
    } catch (error) {
        console.error('Fetch failed:', error);
        throw error;
    }
};

export { fetchWithJWT };