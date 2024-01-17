import React, { useEffect, useState } from 'react';
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import { useUserContext } from "../../pages/usercontext/UserContext";

const PostNotification = ({ postId }) => {
    const { user } = useUserContext();
    const [postNotificationCount, setPostNotificationCount] = useState(0);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!user || !user.appUserID || !postId) {
            return;
        }

        const url = `/api/notifications/posts/${user.appUserID}/${postId}/getIndividualPostNotification`;

        const fetchPostNotifications = async () => {
            try {
                const response = await fetchWithJWT(url);

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const count = await response.json();
                setPostNotificationCount(count);
            } catch (error) {
                setError(error.message);
            }
        };

        fetchPostNotifications();


    }, [user, postId]);

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
                <div>Error fetching post notifications: {error}</div>
            ) : (
                PostNotification > 0 && (
                    <div style={notificationStyle}>
                        {postNotificationCount}
                    </div>
                )
            )}
        </div>
    );
};

export default PostNotification;