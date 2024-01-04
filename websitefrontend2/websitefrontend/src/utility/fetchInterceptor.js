import Cookies from 'js-cookie';

export const fetchWithJWT = (url, options = {}) => {
    let token = Cookies.get('jwt');

    // Trim whitespace from the token
    if (token) {
        token = token.trim();
    }

    console.log(token);
    const headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`
    };

    return fetch(url, { ...options, headers });
};