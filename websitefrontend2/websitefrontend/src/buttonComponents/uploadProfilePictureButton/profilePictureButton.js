import React, { useState } from 'react';
import {user, useUserContext} from "../../../src/pages/usercontext/UserContext"

const UploadProfilePictureButton = () => {
    const [error, setError] = useState('');
    const [status, setStatus] = useState('');
    const [base64Image, setBase64Image] = useState('');
    const {user} = useUserContext();
    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                // Ensure reader.result is treated as a string
                if (typeof reader.result === 'string') {
                    setBase64Image(reader.result);
                } else {
                    // Handle the case where reader.result is not a string
                    console.error('FileReader result is not a string');
                    setError('Error reading file');
                }
            };
            reader.readAsDataURL(file);
        }
    };

    const uploadProfilePictureClick = async () => {
        try {
            const response = await fetch(`/api/account/${user.appUserID}/uploadProfilePicture`, {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ image: base64Image }),
            });
            if (response.ok) {
                const data = await response.json();
                setStatus(response.status);
            } else {
                throw new Error('Network response was not ok.');
            }
        } catch (error) {
            console.error(error);
            setError("An error has occurred");
        }
    };

    return (
        <div>
            <input accept="image/*" id="icon-button-file" type="file" onChange={handleFileChange}/>
            <button onClick={uploadProfilePictureClick}>Upload Picture</button>
            <p>YO</p>
            {error && <div>Error: {error}</div>}
            {status && <div>Status: {status}</div>}
        </div>
    );
};

export default UploadProfilePictureButton;