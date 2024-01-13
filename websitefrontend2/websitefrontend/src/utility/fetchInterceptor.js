import Cookies from 'js-cookie';
import { showSessionExpiredOverlay } from './sessionExpiredOverlay';
const fetchWithJWT = async (url, options = {}) => {
    let token = Cookies.get('jwt');

    if (token) {
        token = token.trim();
    }

    const headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`,
    };

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

export {fetchWithJWT};