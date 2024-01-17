import React from 'react'; // React must be 'react' (lowercase)
import { useUserContext } from "../../pages/usercontext/UserContext";
import { useState } from "react";
import { fetchWithJWT } from "../../utility/fetchInterceptor";
import {getRandomColor} from "../../FunSFX/randomColorGenerator";

const UnfollowButton = ({ followedId }) => {
    const [error, setError] = useState('');
    const { user } = useUserContext();
    const userid = user.appUserID;

    const buttonStyle = {
        backgroundColor: user && user.backgroundColor ? user.backgroundColor : getRandomColor(),
        color: '#FFFFFF',
        border: '4px solid black',
        height: '50px',
        width: '250px',
        margin: '10px',
    };

    const handleButtonClick = async () => {
        const url = "/api/unfollow";
        const params = new URLSearchParams({
            userId: userid,
            followedId: followedId
        });
        try {
            const response = await fetchWithJWT(`${url}?${params.toString()}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            });

            if (response.ok) {
                console.log(`${userid} unfollowing ${followedId} was a success`);
            } else {
                const errorText = await response.text();
                setError(`Failed to unfollow: ${errorText}`);
            }
        } catch (error) {
            setError(`Error sending message: ${error.message}`);
        }
    };



    return (
        <div>
            <button onClick={handleButtonClick} style={buttonStyle}>Unfollow</button>
            {error &&
                <p>Error unfollowing user: {error}</p>
            }
        </div>
    );
};

export default UnfollowButton;