import React, { useEffect, useState } from 'react';
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import { useUserContext } from "../../pages/usercontext/UserContext";

const MessageNotifications = () => {
    const { user } = useUserContext();
    const [messageCount, setMessageCount] = useState(0);
    const [error, setError] = useState(null);
    const url = `/api/message/${user.appUserID}/getMessageNotifications`;

    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const response = await fetchWithJWT(url, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();
                setMessageCount(data);
            } catch (error) {
                setError(error.message);
            }
        };

        const interval = setInterval(() => {
            fetchMessages();
        }, 5000);

        return () => clearInterval(interval);
    }, [url]);

    const notificationStyle = {
        backgroundColor: 'red',
        color: 'white',
        borderRadius: '50%',
        width: '30px',
        height: '30px',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        fontWeight: 'bold',
        position: 'absolute',
        transform: 'translateX(55px) translateY(10px)'
    };

    return (
        <div>
            {error ? (
                <div>Error fetching messages: {error}</div>
            ) : (
                messageCount > 0 && (
                    <div style={notificationStyle}>
                        {messageCount}
                    </div>
                )
            )}
        </div>
    );
};

export default MessageNotifications;