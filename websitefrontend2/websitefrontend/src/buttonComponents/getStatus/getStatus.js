import React, { useState, useEffect } from 'react';
import { fetchWithJWT } from "../../utility/fetchInterceptor";

const UserStatus = ({ appUserId, style }) => {
    const [status, setStatus] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchWithJWT(`/api/status/getByUser/${appUserId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                setStatus(data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
                setError(error);
                setLoading(false);
            });
    }, [appUserId]);

    if (loading) {
        return <div style={style}>Loading status...</div>;
    }

    if (error) {
        return null;
    }

    return (
        <div style={style}>
            {status ? (
                <div>"{status.content}"</div>
            ) : (
                <p>No status available.</p>
            )}
        </div>
    );
};

export default UserStatus;