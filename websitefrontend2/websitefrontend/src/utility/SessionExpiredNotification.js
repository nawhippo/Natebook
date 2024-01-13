import React, { useState, useEffect } from 'react';
import { useUserContext } from "../pages/usercontext/UserContext";
import { getRandomColor } from "../FunSFX/randomColorGenerator";
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';
import './sessionExpiredOverlay.css';
import {registerOverlayVisibilityCallback} from "./sessionExpiredOverlay";

const SessionExpiredNotification = () => {
    const [visible, setVisible] = useState(false);
    const { user, clearUserContext } = useUserContext();
    const history = useHistory();

    useEffect(() => {
        registerOverlayVisibilityCallback(setVisible);
    }, []);

    const handleLogout = () => {
        const userData = Cookies.get('userData');
        const userId = userData ? JSON.parse(userData).appUserID : null;

        if (userId) {
            fetch(`/api/${userId}/logout`, { method: "POST" })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Response was not ok!');
                    }
                    console.log('Logged out due to session expiration');
                    performCleanup();
                })
                .catch(error => {
                    console.error('Logout failed:', error);
                    performCleanup();
                });
        } else {
            console.error('No user data found for logout');
            performCleanup();
        }
    };

    const performCleanup = () => {
        Cookies.remove('userData');
        Cookies.remove('jwt');
        clearUserContext();
        history.push('/AllUsersPage');
        window.location.reload();
    };

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        color: '#FFFFFF',
        border: '4px solid black',
    };

    if (!visible) {
        return null;
    }

    return (
        <div className="session-expired-overlay">
            <div className="session-expired-content">
                <h2>Session Expired</h2>
                <p>Your session has expired. Please log in again.</p>
                <button style={buttonStyle} onClick={handleLogout}>Close and Logout</button>
            </div>
        </div>
    );
};

export default SessionExpiredNotification;