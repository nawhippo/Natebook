import React, { useEffect, useState } from 'react';
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import { useUserContext } from "../../pages/usercontext/UserContext";

const ThreadNotification = ({ threadId }) => {
    const { user } = useUserContext();
    const [threadNotificationCount, setThreadNotificationCount] = useState(0);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!user || !user.appUserID || !threadId) {
            return;
        }

        const url = `/api/message/${threadId}/getThreadNotificationsCount`;

        const fetchThreadNotifications = async () => {
            try {
                const response = await fetchWithJWT(url);

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const count = await response.json();
                setThreadNotificationCount(count);
            } catch (error) {
                setError(error.message);
            }
        };

        fetchThreadNotifications();


    }, [user, threadId]);

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
    };

    return (
        <div>
            {error ? (
                <div>Error fetching thread notifications: {error}</div>
            ) : (
                threadNotificationCount > 0 && (
                    <div style={notificationStyle}>
                        {threadNotificationCount}
                    </div>
                )
            )}
        </div>
    );
};

export default ThreadNotification;