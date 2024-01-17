import React, { useState, useEffect } from 'react';
import {fetchWithJWT} from "../../utility/fetchInterceptor";

const GoogleAccountDetails = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchWithJWT('/api/account/getGoogleAccountDetails')
            .then(response => response.json())
            .then(data => {
                setFirstName(data.firstName);
                setLastName(data.lastName);
                setEmail(data.email);
            })
            .catch(error => {
                console.error('Error:', error);
                setError(error);
            });
    }, []);

    return (
        <div>
            <h1>Google Account Details</h1>
            {error ? (
                <div>Error: {error.message}</div>
            ) : (
                <div>
                    <p>First Name: {firstName}</p>
                    <p>Last Name: {lastName}</p>
                    <p>Email: {email}</p>
                </div>
            )}
        </div>
    );
};

export default GoogleAccountDetails;